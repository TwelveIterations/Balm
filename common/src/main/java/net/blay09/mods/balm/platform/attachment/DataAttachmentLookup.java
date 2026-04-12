package net.blay09.mods.balm.platform.attachment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jspecify.annotations.Nullable;

public interface DataAttachmentLookup<T> {
    @Nullable
    T get(Player player);

    boolean has(Player player);

    @Nullable
    T remove(Player player);

    @Nullable
    T update(Player player, T value);

    @Nullable
    T get(Level level);

    boolean has(Level level);

    @Nullable
    T remove(Level level);

    @Nullable
    T update(Level level, T value);

    @Nullable
    T get(Entity entity);

    boolean has(Entity entity);

    @Nullable
    T remove(Entity entity);

    @Nullable
    T update(Entity entity, T value);

    @Nullable
    T get(BlockEntity blockEntity);

    boolean has(BlockEntity blockEntity);

    @Nullable
    T remove(BlockEntity blockEntity);

    @Nullable
    T update(BlockEntity blockEntity, T value);

    @Nullable
    T get(ChunkAccess chunk);

    boolean has(ChunkAccess chunk);

    @Nullable
    T remove(ChunkAccess chunk);

    @Nullable
    T update(ChunkAccess chunk, T value);
}
