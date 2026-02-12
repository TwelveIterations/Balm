package net.blay09.mods.balm.fabric.network.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.network.internal.CommonBalmNetworking;
import net.blay09.mods.balm.network.protocol.common.custom.ClientboundMessageRegistration;
import net.blay09.mods.balm.network.protocol.common.custom.ServerboundMessageRegistration;
import net.blay09.mods.balm.network.protocol.common.custom.internal.MessageRegistration;
import net.blay09.mods.balm.world.BalmMenuProvider;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class FabricBalmNetworking extends CommonBalmNetworking {

    private static final Logger logger = LoggerFactory.getLogger(FabricBalmNetworking.class);

    private static final Map<CustomPacketPayload.Type<? extends CustomPacketPayload>, MessageRegistration<RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> messagesByType = new ConcurrentHashMap<>();
    @Nullable
    private static PacketSender replyPacketSender;

    public static void initializeClientHandlers() {
        for (final var messageRegistration : messagesByType.values()) {
            if (messageRegistration instanceof ClientboundMessageRegistration<RegistryFriendlyByteBuf, ?> clientboundMessageRegistration) {
                registerClientHandler(clientboundMessageRegistration);
            }
        }
    }

    private static <TPayload extends CustomPacketPayload> void registerClientHandler(ClientboundMessageRegistration<RegistryFriendlyByteBuf, TPayload> messageRegistration) {
        final var type = messageRegistration.getType();
        BiConsumer<Player, TPayload> handler = messageRegistration.getHandler();
        ClientPlayNetworking.registerGlobalReceiver(type, ((payload, context) -> context.client().execute(() -> handler.accept(context.player(), payload))));
    }

    @Override
    public void openMenu(Player player, MenuProvider menuProvider) {
        if (menuProvider instanceof BalmMenuProvider<?> balmMenuProvider) {
            player.openMenu(new ExtendedScreenHandlerFactory<>() {
                @Override
                public Object getScreenOpeningData(ServerPlayer player) {
                    return balmMenuProvider.getScreenOpeningData(player);
                }

                @Override
                public Component getDisplayName() {
                    return balmMenuProvider.getDisplayName();
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                    return balmMenuProvider.createMenu(i, inventory, player);
                }
            });
        } else {
            player.openMenu(menuProvider);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void reply(T message) {
        if (replyPacketSender == null) {
            throw new IllegalStateException("No context to reply to");
        }

        replyPacketSender.sendPacket(message);
    }

    @Override
    public <T extends CustomPacketPayload> void sendTo(Player player, T message) {
        if (player instanceof ServerPlayer serverPlayer && isMessageSupported(serverPlayer, message)) {
            ServerPlayNetworking.send(serverPlayer, message);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToTracking(ServerLevel world, BlockPos pos, T message) {
        for (final var player : PlayerLookup.tracking(world, pos)) {
            if (isMessageSupported(player, message)) {
                ServerPlayNetworking.send(player, message);
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToTracking(Entity entity, T message) {
        for (final var player : PlayerLookup.tracking(entity)) {
            if (isMessageSupported(player, message)) {
                ServerPlayNetworking.send(player, message);
            }
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToAll(MinecraftServer server, T message) {
        for (final var player : PlayerLookup.all(server)) {
            if (isMessageSupported(player, message)) {
                ServerPlayNetworking.send(player, message);
            }
        }
    }

    @Override
    public boolean isMessageSupported(ServerPlayer player, CustomPacketPayload payload) {
        // Short-circuit to Fabric's inbuilt check, but if the mod is announced (super impl) we send it regardless
        // That way we error explicitly on an illegal state rather than letting the issue propagate into undefined behavior
        return ServerPlayNetworking.canSend(player, payload.type()) || super.isMessageSupported(player, payload);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T message) {
        if (!Balm.safeClientAccess().isConnected()) {
            logger.debug("Skipping message {} because we're not connected to a server", message);
        } else if (isMessageSupportedByServer(message)) {
            ClientPlayNetworking.send(message);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void registerClientboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<Player, T> handler) {
        registeredMods.add(type.id().getNamespace());
        final var messageRegistration = new ClientboundMessageRegistration<>(type, codec, handler);
        PayloadTypeRegistry.playS2C().register(type, messageRegistration.getCodec());
        messagesByType.put(type, messageRegistration);
    }

    @Override
    public <T extends CustomPacketPayload> void registerServerboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<ServerPlayer, T> handler) {
        registeredMods.add(type.id().getNamespace());
        final var messageRegistration = new ServerboundMessageRegistration<>(type, codec, handler);
        messagesByType.put(type, messageRegistration);

        PayloadTypeRegistry.playC2S().register(type, messageRegistration.getCodec());
        ServerPlayNetworking.registerGlobalReceiver(type, ((payload, context) -> context.server().execute(() -> {
            replyPacketSender = context.responseSender();
            handler.accept(context.player(), payload);
            replyPacketSender = null;
        })));
    }

}
