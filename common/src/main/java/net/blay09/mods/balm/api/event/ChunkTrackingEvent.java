package net.blay09.mods.balm.api.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

public abstract class ChunkTrackingEvent {
    private final ServerLevel level;
    private final ServerPlayer player;
    private final ChunkPos chunkPos;

    public ChunkTrackingEvent(ServerLevel level, ServerPlayer player, ChunkPos chunkPos) {
        this.level = level;
        this.player = player;
        this.chunkPos = chunkPos;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public static class Start extends ChunkTrackingEvent {
        public Start(ServerLevel level, ServerPlayer player, ChunkPos chunkPos) {
            super(level, player, chunkPos);
        }
    }

    public static class Stop extends ChunkTrackingEvent {
        public Stop(ServerLevel level, ServerPlayer player, ChunkPos chunkPos) {
            super(level, player, chunkPos);
        }
    }

}
