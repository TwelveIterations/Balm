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

    @Nullable
    T get(Level level);

    @Nullable
    T get(Entity entity);

    @Nullable
    T get(BlockEntity blockEntity);

    @Nullable
    T get(ChunkAccess chunk);
}
