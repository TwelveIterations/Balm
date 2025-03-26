package net.blay09.mods.balm.fabric.network;

import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.balm.api.network.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FabricBalmNetworking implements BalmNetworking {

    private static final Map<CustomPacketPayload.Type<? extends CustomPacketPayload>, MessageRegistration<RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> messagesByType = new HashMap<>();
    private static PacketSender replyPacketSender;
    private final Set<String> registeredMods = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> clientOnlyMods = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> serverOnlyMods = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, String> networkVersions = Collections.synchronizedMap(new HashMap<>());

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

    public Set<String> getRegisteredMods() {
        return registeredMods;
    }

    public boolean isClientOnly(String modId) {
        return clientOnlyMods.contains(modId);
    }

    public boolean isServerOnly(String modId) {
        return serverOnlyMods.contains(modId);
    }

    @Override
    public void allowClientOnly(String modId) {
        clientOnlyMods.add(modId);
    }

    @Override
    public void allowServerOnly(String modId) {
        serverOnlyMods.add(modId);
    }

    @Override
    public void openGui(Player player, MenuProvider menuProvider) {
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
    public void defineNetworkVersion(String modId, String version) {
        networkVersions.put(modId, version);
    }

    public Optional<NetworkVersions> getNetworkVersions(String modId) {
        return FabricLoader.getInstance().getModContainer(modId)
                .map(modContainer -> modContainer.getMetadata().getVersion().toString())
                .map(modVersion -> {
                    final var networkVersion = networkVersions.getOrDefault(modId, modVersion);
                    return new NetworkVersions(modVersion, networkVersion);
                });
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
        ServerPlayNetworking.send((ServerPlayer) player, message);
    }

    @Override
    public <T extends CustomPacketPayload> void sendToTracking(ServerLevel world, BlockPos pos, T message) {
        for (ServerPlayer player : PlayerLookup.tracking(world, pos)) {
            ServerPlayNetworking.send(player, message);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToTracking(Entity entity, T message) {
        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, message);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToAll(MinecraftServer server, T message) {
        for (ServerPlayer player : PlayerLookup.all(server)) {
            ServerPlayNetworking.send(player, message);
        }
    }

    @Override
    public <T extends CustomPacketPayload> void sendToServer(T message) {
        ClientPlayNetworking.send(message);
    }

    @Override
    public <T extends CustomPacketPayload> void registerClientboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, BiConsumer<RegistryFriendlyByteBuf, T> encodeFunc, Function<RegistryFriendlyByteBuf, T> decodeFunc, BiConsumer<Player, T> handler) {
        registeredMods.add(type.id().getNamespace());
        final var messageRegistration = new ClientboundMessageRegistration<>(type, clazz, encodeFunc, decodeFunc, handler);
        PayloadTypeRegistry.playS2C().register(type, messageRegistration.getCodec());
        messagesByType.put(type, messageRegistration);
    }

    @Override
    public <T extends CustomPacketPayload> void registerServerboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, BiConsumer<RegistryFriendlyByteBuf, T> encodeFunc, Function<RegistryFriendlyByteBuf, T> decodeFunc, BiConsumer<ServerPlayer, T> handler) {
        registeredMods.add(type.id().getNamespace());
        final var messageRegistration = new ServerboundMessageRegistration<>(type, clazz, encodeFunc, decodeFunc, handler);
        messagesByType.put(type, messageRegistration);

        PayloadTypeRegistry.playC2S().register(type, messageRegistration.getCodec());
        ServerPlayNetworking.registerGlobalReceiver(type, ((payload, context) -> context.player().getServer().execute(() -> {
            replyPacketSender = context.responseSender();
            handler.accept(context.player(), payload);
            replyPacketSender = null;
        })));
    }

}
