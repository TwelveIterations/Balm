package net.blay09.mods.balm.mixin;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.ChunkTrackingEvent;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public class ChunkMapMixin {

    @Inject(method = "markChunkPendingToSend(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/chunk/LevelChunk;)V", at = @At(value = "HEAD"))
    private static void markChunkPendingToSend(ServerPlayer player, LevelChunk chunk, CallbackInfo callbackInfo) {
        Balm.getEvents().fireEvent(new ChunkTrackingEvent.Start((ServerLevel) chunk.getLevel(), player, chunk.getPos()));
    }

    @Inject(method = "dropChunk(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/level/ChunkPos;)V", at = @At(value = "HEAD"))
    private static void dropChunk(ServerPlayer player, ChunkPos chunkPos, CallbackInfo callbackInfo) {
        Balm.getEvents().fireEvent(new ChunkTrackingEvent.Stop(player.serverLevel(), player, chunkPos));
    }
}
