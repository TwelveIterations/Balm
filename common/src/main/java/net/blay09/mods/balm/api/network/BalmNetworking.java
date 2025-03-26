package net.blay09.mods.balm.api.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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

    default void allowClientAndServerOnly(String modId) {
        allowClientOnly(modId);
        allowServerOnly(modId);
    }

    void allowClientOnly(String modId);

    void allowServerOnly(String modId);

    <T> void reply(T message);

    <T> void sendTo(Player player, T message);

    <T> void sendToTracking(ServerLevel world, BlockPos pos, T message);

    <T> void sendToTracking(Entity entity, T message);

    <T> void sendToAll(MinecraftServer server, T message);

    <T> void sendToServer(T message);

    <T> void registerClientboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<Player, T> handler);

    <T> void registerServerboundPacket(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<ServerPlayer, T> handler);
}
