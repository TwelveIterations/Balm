package net.blay09.mods.balm.neoforge.network.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.internal.mixin.ChunkMapAccessor;
import net.blay09.mods.balm.neoforge.platform.event.internal.ModBusEventRegisters;
import net.blay09.mods.balm.network.internal.CommonBalmNetworking;
import net.blay09.mods.balm.network.protocol.common.custom.ClientboundMessageRegistration;
import net.blay09.mods.balm.network.protocol.common.custom.ServerboundMessageRegistration;
import net.blay09.mods.balm.network.protocol.common.custom.internal.MessageRegistration;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class NeoForgeBalmNetworking extends CommonBalmNetworking {

    private static final Logger logger = LoggerFactory.getLogger(NeoForgeBalmNetworking.class);
    @Nullable
    private static IPayloadContext replyContext;

    @Override
    public void openMenu(Player player, MenuProvider menuProvider) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (menuProvider instanceof BalmMenuProvider<?> balmMenuProvider) {
                openGui(serverPlayer, balmMenuProvider);
            } else {
                serverPlayer.openMenu(menuProvider);
            }
        }
    }

    private <T> void openGui(ServerPlayer player, BalmMenuProvider<T> menuProvider) {
        player.openMenu(menuProvider, buf -> menuProvider.getScreenStreamCodec().encode(buf, menuProvider.getScreenOpeningData(player)));
    }

    @Override
    public <T extends CustomPacketPayload> void reply(T message) {
        if (replyContext == null) {
            throw new IllegalStateException("No context to reply to");
        }

        replyContext.reply(message);
    }

    @Override
    public <T extends CustomPacketPayload> void sendTo(Player player, T message) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (isMessageSupported(serverPlayer, message)) {
                PacketDistributor.sendToPlayer(serverPlayer, message);
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToTracking(ServerLevel level, BlockPos pos, T message) {
        final var players = level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false);
        for (final var player : players) {
            if (isMessageSupported(player, message)) {
                PacketDistributor.sendToPlayer(player, message);
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToTracking(Entity entity, T message) {
        if (entity.level() instanceof ServerLevel level) {
            final var trackedEntity = ((ChunkMapAccessor) level.getChunkSource().chunkMap).getEntityMap().get(entity.getId());
            for(final var connection : trackedEntity.getSeenBy()) {
                final var player = connection.getPlayer();
                if (isMessageSupported(player, message)) {
                    PacketDistributor.sendToPlayer(player, message);
                }
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToAll(MinecraftServer server, T message) {
        for (final var player : server.getPlayerList().getPlayers()) {
            if (isMessageSupported(player, message)) {
                PacketDistributor.sendToPlayer(player, message);
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T message) {
        if (!Balm.safeClientAccess().isConnected()) {
            logger.debug("Skipping message {} because we're not connected to a server", message);
            return;
        }

        if (isMessageSupportedByServer(message)) {
            ClientPacketDistributor.sendToServer(message);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void registerClientboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<Player, T> handler) {
        final var messageRegistration = new ClientboundMessageRegistration<>(type, codec, handler);
        final var registrations = getActiveRegistrations(type.id().getNamespace());
        registrations.playMessagesByType.put(type, messageRegistration);
        registeredMods.add(type.id().getNamespace());
    }

    @Override
    public <T extends CustomPacketPayload> void registerServerboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<ServerPlayer, T> handler) {
        final var messageRegistration = new ServerboundMessageRegistration<>(type, codec, handler);
        final var registrations = getActiveRegistrations(type.id().getNamespace());
        registrations.playMessagesByType.put(type, messageRegistration);
        registeredMods.add(type.id().getNamespace());
    }

    private Registrations getActiveRegistrations(String namespace) {
        return ModBusEventRegisters.getRegistrations(namespace, Registrations.class);
    }

    public static class Registrations {
        private final String modId;
        private final Map<CustomPacketPayload.Type<? extends CustomPacketPayload>, MessageRegistration<RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> playMessagesByType = new ConcurrentHashMap<>();

        public Registrations(String modId) {
            this.modId = modId;
        }

        @SubscribeEvent
        public void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
            final var networking = (CommonBalmNetworking) Balm.networking();
            final var networkVersion = networking.getNetworkVersion(modId);
            var registrar = event.registrar(networkVersion != null ? networkVersion : modId);
            if (networking.isClientOnly(modId) || networking.isServerOnly(modId)) {
                registrar = registrar.optional();
            }
            for (final var entry : playMessagesByType.entrySet()) {
                final var messageRegistration = entry.getValue();
                if (messageRegistration instanceof ServerboundMessageRegistration<RegistryFriendlyByteBuf, ? extends CustomPacketPayload> serverboundMessageRegistration) {
                    registrar = playToServer(registrar, serverboundMessageRegistration);
                } else if (messageRegistration instanceof ClientboundMessageRegistration<RegistryFriendlyByteBuf, ? extends CustomPacketPayload> clientboundMessageRegistration) {
                    registrar = playToClient(registrar, clientboundMessageRegistration);
                }
            }
        }

        private <TPayload extends CustomPacketPayload> PayloadRegistrar playToServer(PayloadRegistrar registrar, ServerboundMessageRegistration<RegistryFriendlyByteBuf, TPayload> registration) {
            return registrar.playToServer(registration.getType(), registration.getCodec(), createPayloadHandler(registration));
        }

        private <TPayload extends CustomPacketPayload> PayloadRegistrar playToClient(PayloadRegistrar registrar, ClientboundMessageRegistration<RegistryFriendlyByteBuf, TPayload> registration) {
            return registrar.playToClient(registration.getType(), registration.getCodec(), createPayloadHandler(registration));
        }

        private <TBuffer extends FriendlyByteBuf, TPayload extends CustomPacketPayload> IPayloadHandler<TPayload> createPayloadHandler(ServerboundMessageRegistration<TBuffer, TPayload> serverboundMessageRegistration) {
            return new MainThreadPayloadHandler<>((payload, context) -> {
                replyContext = context;
                serverboundMessageRegistration.getHandler().accept((ServerPlayer) context.player(), payload);
                replyContext = null;
            });
        }

        private <TBuffer extends FriendlyByteBuf, TPayload extends CustomPacketPayload> IPayloadHandler<TPayload> createPayloadHandler(ClientboundMessageRegistration<TBuffer, TPayload> clientboundMessageRegistration) {
            return new MainThreadPayloadHandler<>((payload, context) -> {
                replyContext = context;
                clientboundMessageRegistration.getHandler().accept(context.player(), payload);
                replyContext = null;
            });
        }
    }
}