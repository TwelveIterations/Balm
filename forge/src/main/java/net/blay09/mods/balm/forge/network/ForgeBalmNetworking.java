package net.blay09.mods.balm.forge.network;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.network.ClientboundMessageRegistration;
import net.blay09.mods.balm.api.network.MessageRegistration;
import net.blay09.mods.balm.api.network.ServerboundMessageRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ForgeBalmNetworking implements BalmNetworking {

    private static final Logger logger = LoggerFactory.getLogger(ForgeBalmNetworking.class);

    private static final Map<Class<?>, MessageRegistration<?>> messagesByClass = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, MessageRegistration<?>> messagesByIdentifier = new ConcurrentHashMap<>();
    private static final Map<String, Integer> discriminatorCounter = new ConcurrentHashMap<>();

    private static NetworkEvent.Context replyContext;

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
    public void openGui(Player player, MenuProvider menuProvider) {
        if (player instanceof ServerPlayer) {
            if (menuProvider instanceof BalmMenuProvider balmMenuProvider) {
                NetworkHooks.openScreen((ServerPlayer) player, menuProvider, buf -> balmMenuProvider.writeScreenOpeningData((ServerPlayer) player, buf));
            } else {
                NetworkHooks.openScreen((ServerPlayer) player, menuProvider);
            }
        }
    }

    @Override
    public void defineNetworkVersion(String modId, String version) {
        NetworkChannels.defineNetworkVersion(modId, version);
    }

    @Override
    public <T> void reply(T message) {
        if (replyContext == null) {
            throw new IllegalStateException("No context to reply to");
        }

        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);
        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.reply(message, replyContext);
    }

    @Override
    public <T> void sendTo(Player player, T message) {
        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);

        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(PacketDistributor.PLAYER.with(() -> ((ServerPlayer) player)), message);
    }

    @Override
    public <T> void sendToTracking(ServerLevel world, BlockPos pos, T message) {
        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);

        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), message);
    }

    @Override
    public <T> void sendToTracking(Entity entity, T message) {
        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);

        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    @Override
    public <T> void sendToAll(MinecraftServer server, T message) {
        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);

        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.send(PacketDistributor.ALL.noArg(), message);
    }

    @Override
    public <T> void sendToServer(T message) {
        if (!Balm.getProxy().isConnectedToServer()) {
            logger.debug("Skipping message {} because we're not connected to a server", message);
            return;
        }

        MessageRegistration<T> messageRegistration = getMessageRegistrationOrThrow(message);

        ResourceLocation identifier = messageRegistration.getIdentifier();

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.sendToServer(message);
    }

    @SuppressWarnings("unchecked")
    private <T> MessageRegistration<T> getMessageRegistrationOrThrow(T message) {
        MessageRegistration<T> messageRegistration = (MessageRegistration<T>) messagesByClass.get(message.getClass());
        if (messageRegistration == null) {
            throw new IllegalArgumentException("Cannot send message " + message.getClass() + " as it is not registered");
        }
        return messageRegistration;
    }

    @Override
    public <T> void registerClientboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<Player, T> handler) {
        ClientboundMessageRegistration<T> messageRegistration = new ClientboundMessageRegistration<>(identifier, clazz, encodeFunc, decodeFunc, handler);

        messagesByClass.put(clazz, messageRegistration);
        messagesByIdentifier.put(identifier, messageRegistration);

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.registerMessage(nextDiscriminator(identifier.getNamespace()), clazz, encodeFunc, decodeFunc, (message, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
                logger.warn("Received {} on incorrect side {}", identifier, context.getDirection());
                return;
            }

            context.enqueueWork(() -> {
                handler.accept(BalmClient.getClientPlayer(), message);
            });
            context.setPacketHandled(true);
        });
    }

    @Override
    public <T> void registerServerboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<ServerPlayer, T> handler) {
        MessageRegistration<T> messageRegistration = new ServerboundMessageRegistration<>(identifier, clazz, encodeFunc, decodeFunc, handler);

        messagesByClass.put(clazz, messageRegistration);
        messagesByIdentifier.put(identifier, messageRegistration);

        SimpleChannel channel = NetworkChannels.get(identifier.getNamespace());
        channel.registerMessage(nextDiscriminator(identifier.getNamespace()), clazz, encodeFunc, decodeFunc, (message, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection() != NetworkDirection.PLAY_TO_SERVER) {
                logger.warn("Received {} on incorrect side {}", identifier, context.getDirection());
                return;
            }

            context.enqueueWork(() -> {
                replyContext = context;
                ServerPlayer player = context.getSender();
                handler.accept(player, message);
                replyContext = null;
            });
            context.setPacketHandled(true);
        });
    }
}
