package net.blay09.mods.balm.fabric.platform.attachment.internal;

import net.blay09.mods.balm.platform.attachment.DataAttachmentLookup;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jspecify.annotations.Nullable;

public class FabricDataAttachmentLookup<T> implements DataAttachmentLookup<T> {

    private final AttachmentType<T> type;

    public FabricDataAttachmentLookup(AttachmentType<T> type) {
        this.type = type;
    }

    @Override
    public @Nullable T get(Player player) {
        return player.getAttached(type);
    }

    @Override
    public @Nullable T get(Level level) {
        return level.getAttached(type);
    }

    @Override
    public @Nullable T get(Entity entity) {
        return entity.getAttached(type);
    }

    @Override
    public @Nullable T get(BlockEntity blockEntity) {
        return blockEntity.getAttached(type);
    }

    @Override
    public @Nullable T get(ChunkAccess chunk) {
        return chunk.getAttached(type);
    }
}
