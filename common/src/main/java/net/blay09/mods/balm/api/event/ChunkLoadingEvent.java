package net.blay09.mods.balm.api.event;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.LevelCallback.Chunk} instead.
 */
@Deprecated
public abstract class ChunkLoadingEvent {
    private final LevelAccessor level;
    private final ChunkAccess chunk;
    private final ChunkPos chunkPos;

    public ChunkLoadingEvent(LevelAccessor level, ChunkAccess chunk) {
        this.level = level;
        this.chunk = chunk;
        this.chunkPos = chunk.getPos();
    }

    public LevelAccessor getLevel() {
        return level;
    }

    public ChunkAccess getChunk() {
        return chunk;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.event.callback.LevelCallback.Chunk#LOAD} instead.
     */
    @Deprecated
    public static class Load extends ChunkLoadingEvent {
        public Load(LevelAccessor level, ChunkAccess chunk) {
            super(level, chunk);
        }
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.event.callback.LevelCallback.Chunk#UNLOAD} instead.
     */
    @Deprecated
    public static class Unload extends ChunkLoadingEvent {
        public Unload(LevelAccessor level, ChunkAccess chunk) {
            super(level, chunk);
        }
    }

}
