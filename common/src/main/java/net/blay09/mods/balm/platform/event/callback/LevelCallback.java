package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;

@FunctionalInterface
public interface LevelCallback {
    void handle(LevelAccessor level);

    EventMapper<LevelCallback> LOAD = EventMapper.createUnbound("LevelCallback.LOAD");
    EventMapper<LevelCallback> UNLOAD = EventMapper.createUnbound("LevelCallback.UNLOAD");

    @FunctionalInterface
    interface Chunk {
        void handle(LevelAccessor level, ChunkAccess chunk, ChunkPos chunkPos);

        EventMapper<Chunk> LOAD = EventMapper.createUnbound("LevelCallback.Chunk.LOAD");
        EventMapper<Chunk> UNLOAD = EventMapper.createUnbound("LevelCallback.Chunk.UNLOAD");
    }

}
