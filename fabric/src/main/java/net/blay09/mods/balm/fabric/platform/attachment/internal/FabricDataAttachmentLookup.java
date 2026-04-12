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
    public boolean has(Player player) {
        return player.hasAttached(type);
    }

    @Override
    public @Nullable T remove(Player player) {
        return player.removeAttached(type);
    }

    @Override
    public @Nullable T update(Player player, T value) {
        return player.setAttached(type, value);
    }

    @Override
    public @Nullable T get(Level level) {
        return level.getAttached(type);
    }

    @Override
    public boolean has(Level level) {
        return level.hasAttached(type);
    }

    @Override
    public @Nullable T remove(Level level) {
        return level.removeAttached(type);
    }

    @Override
    public @Nullable T update(Level level, T value) {
        return level.setAttached(type, value);
    }

    @Override
    public @Nullable T get(Entity entity) {
        return entity.getAttached(type);
    }

    @Override
    public boolean has(Entity entity) {
        return entity.hasAttached(type);
    }

    @Override
    public @Nullable T remove(Entity entity) {
        return entity.removeAttached(type);
    }

    @Override
    public @Nullable T update(Entity entity, T value) {
        return entity.setAttached(type, value);
    }

    @Override
    public @Nullable T get(BlockEntity blockEntity) {
        return blockEntity.getAttached(type);
    }

    @Override
    public boolean has(BlockEntity blockEntity) {
        return blockEntity.hasAttached(type);
    }

    @Override
    public @Nullable T remove(BlockEntity blockEntity) {
        return blockEntity.removeAttached(type);
    }

    @Override
    public @Nullable T update(BlockEntity blockEntity, T value) {
        return blockEntity.setAttached(type, value);
    }

    @Override
    public @Nullable T get(ChunkAccess chunk) {
        return chunk.getAttached(type);
    }

    @Override
    public boolean has(ChunkAccess chunk) {
        return chunk.hasAttached(type);
    }

    @Override
    public @Nullable T remove(ChunkAccess chunk) {
        return chunk.removeAttached(type);
    }

    @Override
    public @Nullable T update(ChunkAccess chunk, T value) {
        return chunk.setAttached(type, value);
    }
}
