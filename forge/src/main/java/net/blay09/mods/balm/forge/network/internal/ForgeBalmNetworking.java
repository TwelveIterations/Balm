package net.blay09.mods.balm.forge.network.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.internal.mixin.ChunkMapAccessor;
import net.blay09.mods.balm.network.internal.CommonBalmNetworking;
import net.blay09.mods.balm.network.protocol.common.custom.ClientboundMessageRegistration;
import net.blay09.mods.balm.network.protocol.common.custom.ServerboundMessageRegistration;
import net.blay09.mods.balm.network.protocol.common.custom.internal.MessageRegistration;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.minecraft.core.BlockPos;
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
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class ForgeBalmNetworking extends CommonBalmNetworking {

    private static final Logger logger = LoggerFactory.getLogger(ForgeBalmNetworking.class);

    private static final Map<CustomPacketPayload.Type<?>, MessageRegistration<RegistryFriendlyByteBuf, ?>> messagesByType = new ConcurrentHashMap<>();
    private static final Map<String, Integer> discriminatorCounter = new ConcurrentHashMap<>();

    private static CustomPayloadEvent.Context replyContext;

    private static int nextDiscriminator(String modId) {
        return discriminatorCounter.compute(modId, (key, prev) -> prev != null ? prev + 1 : 0);
    }

    @Override
    public void allowClientOnly(String modId) {
        super.allowClientOnly(modId);
        NetworkChannels.allowClientOnly(modId);
    }

    @Override
    public void allowServerOnly(String modId) {
        super.allowServerOnly(modId);
        NetworkChannels.allowServerOnly(modId);
    }

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

    @Override
    public void defineNetworkVersion(String modId, String version) {
        super.defineNetworkVersion(modId, version);
        NetworkChannels.defineNetworkVersion(modId, version);
    }

    private <T> void openGui(ServerPlayer player, BalmMenuProvider<T> menuProvider) {
        // TODO we have to create a RegistryFriendlyByteBuf ourselves because Forge is out of date
        player.openMenu(menuProvider,
                buf -> menuProvider.getScreenStreamCodec()
                        .encode(new RegistryFriendlyByteBuf(buf, player.registryAccess()), menuProvider.getScreenOpeningData(player)));
    }

    @Override
    public <T extends CustomPacketPayload> void reply(T message) {
        if (replyContext == null) {
            throw new IllegalStateException("No context to reply to");
        }

        final var channel = NetworkChannels.get(message.type().id().getNamespace());
        channel.reply(message, replyContext);
    }

    @Override
    public <T extends CustomPacketPayload> void sendTo(Player player, T message) {
        if (player instanceof ServerPlayer serverPlayer && isMessageSupported(serverPlayer, message)) {
            final var channel = NetworkChannels.get(message.type().id().getNamespace());
            channel.send(message, serverPlayer.connection.getConnection());
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToTracking(ServerLevel level, BlockPos pos, T message) {
        final var channel = NetworkChannels.get(message.type().id().getNamespace());
        final var players = level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false);
        for (final var player : players) {
            if (isMessageSupported(player, message)) {
                channel.send(message, player.connection.getConnection());
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToTracking(Entity entity, T message) {
        final var channel = NetworkChannels.get(message.type().id().getNamespace());
        if (entity.level() instanceof ServerLevel level) {
            final var trackedEntity = ((ChunkMapAccessor) level.getChunkSource().chunkMap).getEntityMap().get(entity.getId());
            for(final var connection : trackedEntity.getSeenBy()) {
                final var player = connection.getPlayer();
                if (isMessageSupported(player, message)) {
                    channel.send(message, player.connection.getConnection());
                }
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToAll(MinecraftServer server, T message) {
        final var channel = NetworkChannels.get(message.type().id().getNamespace());
        for (final var player : server.getPlayerList().getPlayers()) {
            if (isMessageSupported(player, message)) {
                channel.send(message, player.connection.getConnection());
            }
        }
    }

    @Override
    public boolean isMessageSupported(ServerPlayer player, CustomPacketPayload payload) {
        // Short-circuit to Forge's inbuilt check, but if the mod is announced (super impl) we send it regardless
        // That way we error explicitly on an illegal state rather than letting the issue propagate into undefined behavior
        return NetworkChannels.get(payload.type().id().getNamespace()).isRemotePresent(player.connection.getConnection()) || super.isMessageSupported(player, payload);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T message) {
        if (!Balm.safeClientAccess().isConnected()) {
            logger.debug("Skipping message {} because we're not connected to a server", message);
            return;
        }

        if (isMessageSupportedByServer(message)) {
            final var channel = NetworkChannels.get(message.type().id().getNamespace());
            channel.send(message, PacketDistributor.SERVER.noArg());
        }
    }

    @Override
    public <T extends CustomPacketPayload> void registerClientboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<Player, T> handler) {
        final var messageRegistration = new ClientboundMessageRegistration<>(type, codec, handler);

        messagesByType.put(type, messageRegistration);
        registeredMods.add(type.id().getNamespace());

        SimpleChannel channel = NetworkChannels.get(type.id().getNamespace());
        channel.messageBuilder(clazz, nextDiscriminator(type.id().getNamespace()), NetworkDirection.PLAY_TO_CLIENT)
                .codec(codec)
                .consumerMainThread((packet, context) -> handler.accept(Balm.safeClientAccess().getClientPlayer(), packet))
                .add();
    }

    @Override
    public <T extends CustomPacketPayload> void registerServerboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<ServerPlayer, T> handler) {
        final var messageRegistration = new ServerboundMessageRegistration<>(type, codec, handler);

        messagesByType.put(type, messageRegistration);
        registeredMods.add(type.id().getNamespace());

        final var channel = NetworkChannels.get(type.id().getNamespace());
        channel.messageBuilder(clazz, nextDiscriminator(type.id().getNamespace()), NetworkDirection.PLAY_TO_SERVER)
                .codec(codec)
                .consumerMainThread((packet, context) -> {
                    replyContext = context;
                    handler.accept(context.getSender(), packet);
                    replyContext = null;
                })
                .add();
    }

}
