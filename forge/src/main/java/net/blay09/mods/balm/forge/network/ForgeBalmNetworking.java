package net.blay09.mods.balm.forge.network;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.network.ClientboundMessageRegistration;
import net.blay09.mods.balm.api.network.MessageRegistration;
import net.blay09.mods.balm.api.network.ServerboundMessageRegistration;
import net.blay09.mods.balm.mixin.ChunkMapAccessor;
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

public class ForgeBalmNetworking implements BalmNetworking {

    private static final Logger logger = LoggerFactory.getLogger(ForgeBalmNetworking.class);

    private static final Map<CustomPacketPayload.Type<?>, MessageRegistration<RegistryFriendlyByteBuf, ?>> messagesByType = new ConcurrentHashMap<>();
    private static final Map<String, Integer> discriminatorCounter = new ConcurrentHashMap<>();

    private static CustomPayloadEvent.Context replyContext;

    private static int nextDiscriminator(String modId) {
        return discriminatorCounter.compute(modId, (key, prev) -> prev != null ? prev + 1 : 0);
    }

    @Override
    public void allowClientOnly(String modId) {
        NetworkChannels.allowClientOnly(modId);
    }

    @Override
    public void allowServerOnly(String modId) {
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

        final var messageRegistration = getMessageRegistrationOrThrow(message);
        final var type = messageRegistration.getType();
        final var channel = NetworkChannels.get(type.id().getNamespace());
        channel.reply(message, replyContext);
    }

    @Override
    public <T extends CustomPacketPayload> void sendTo(Player player, T message) {
        final var messageRegistration = getMessageRegistrationOrThrow(message);
        final var type = messageRegistration.getType();
        if (player instanceof ServerPlayer serverPlayer && isMessageSupported(serverPlayer, message)) {
            final var channel = NetworkChannels.get(type.id().getNamespace());
            channel.send(message, PacketDistributor.PLAYER.with(serverPlayer));
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToTracking(ServerLevel level, BlockPos pos, T message) {
        final var messageRegistration = getMessageRegistrationOrThrow(message);
        final var type = messageRegistration.getType();
        final var channel = NetworkChannels.get(type.id().getNamespace());
        final var players = level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false);
        for (final var player : players) {
            if (isMessageSupported(player, message)) {
                channel.send(message, player.connection.getConnection());
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToTracking(Entity entity, T message) {
        final var messageRegistration = getMessageRegistrationOrThrow(message);
        final var type = messageRegistration.getType();
        final var channel = NetworkChannels.get(type.id().getNamespace());
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
        final var messageRegistration = getMessageRegistrationOrThrow(message);
        final var type = messageRegistration.getType();
        final var channel = NetworkChannels.get(type.id().getNamespace());
        for (final var player : server.getPlayerList().getPlayers()) {
            if (isMessageSupported(player, message)) {
                channel.send(message, player.connection.getConnection());
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T message) {
        if (!Balm.getProxy().isConnected()) {
            logger.debug("Skipping message {} because we're not connected to a server", message);
            return;
        }

        final var messageRegistration = getMessageRegistrationOrThrow(message);
        final var type = messageRegistration.getType();
        final var channel = NetworkChannels.get(type.id().getNamespace());
        if (isMessageSupportedByServer(message)) {
            channel.send(message, PacketDistributor.SERVER.noArg());
        }
    }


    @Override
    public boolean isMessageSupported(ServerPlayer player, CustomPacketPayload payload) {
        return NetworkChannels.get(payload.type().id().getNamespace()).isRemotePresent(player.connection.getConnection());
    }

    @Override
    public boolean isMessageSupportedByServer(CustomPacketPayload payload) {
        return NetworkChannels.get(payload.type().id().getNamespace()).isRemotePresent(Balm.safeClientAccess().getConnection());
    }

    @SuppressWarnings("unchecked")
    private <T extends CustomPacketPayload> MessageRegistration<RegistryFriendlyByteBuf, T> getMessageRegistrationOrThrow(T message) {
        final var messageRegistration = (MessageRegistration<RegistryFriendlyByteBuf, T>) messagesByType.get(message.type());
        if (messageRegistration == null) {
            throw new IllegalArgumentException("Cannot send message " + message.getClass() + " as it is not registered");
        }
        return messageRegistration;
    }

    @Override
    public <T extends CustomPacketPayload> void registerClientboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<Player, T> handler) {
        final var messageRegistration = new ClientboundMessageRegistration<>(type, codec, handler);

        messagesByType.put(type, messageRegistration);

        SimpleChannel channel = NetworkChannels.get(type.id().getNamespace());
        channel.messageBuilder(clazz, nextDiscriminator(type.id().getNamespace()), NetworkDirection.PLAY_TO_CLIENT)
                .codec(codec)
                .consumerMainThread((packet, context) -> handler.accept(Balm.getProxy().getClientPlayer(), packet))
                .add();
    }

    @Override
    public <T extends CustomPacketPayload> void registerServerboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<ServerPlayer, T> handler) {
        final var messageRegistration = new ServerboundMessageRegistration<>(type, codec, handler);

        messagesByType.put(type, messageRegistration);

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

    @Override
    public BalmNetworking scoped(String modId) {
        return this;
    }
}
