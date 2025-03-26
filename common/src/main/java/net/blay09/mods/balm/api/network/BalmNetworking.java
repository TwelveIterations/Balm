package net.blay09.mods.balm.api.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface BalmNetworking {
    void openGui(Player player, MenuProvider menuProvider);

    void defineNetworkVersion(String modId, String version);

    default void allowClientAndServerOnly(String modId) {
        allowClientOnly(modId);
        allowServerOnly(modId);
    }

    void allowClientOnly(String modId);

    void allowServerOnly(String modId);

    <T extends CustomPacketPayload> void reply(T message);

    <T extends CustomPacketPayload> void sendTo(Player player, T message);

    <T extends CustomPacketPayload> void sendToTracking(ServerLevel world, BlockPos pos, T message);

    <T extends CustomPacketPayload> void sendToTracking(Entity entity, T message);

    <T extends CustomPacketPayload> void sendToAll(MinecraftServer server, T message);

    <T extends CustomPacketPayload> void sendToServer(T message);

    <T extends CustomPacketPayload> void registerClientboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, BiConsumer<RegistryFriendlyByteBuf, T> encodeFunc, Function<RegistryFriendlyByteBuf, T> decodeFunc, BiConsumer<Player, T> handler);

    <T extends CustomPacketPayload> void registerServerboundPacket(CustomPacketPayload.Type<T> type, Class<T> clazz, BiConsumer<RegistryFriendlyByteBuf, T> encodeFunc, Function<RegistryFriendlyByteBuf, T> decodeFunc, BiConsumer<ServerPlayer, T> handler);
}
