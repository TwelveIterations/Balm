package net.blay09.mods.balm.neoforge.platform.attachment.internal;

import net.blay09.mods.balm.platform.attachment.DataAttachmentLookup;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jspecify.annotations.Nullable;

public class NeoForgeDataAttachmentLookup<T> implements DataAttachmentLookup<T> {
    private final Holder<AttachmentType<T>> type;

    @SuppressWarnings("unchecked")
    public NeoForgeDataAttachmentLookup(Holder<?> type) {
        this.type = (Holder<AttachmentType<T>>) type;
    }

    @Override
    public @Nullable T get(Player player) {
        return player.getExistingDataOrNull(type::value);
    }

    @Override
    public T getOrCreate(Player player) {
        return player.getData(type::value);
    }

    @Override
    public boolean has(Player player) {
        return player.hasData(type::value);
    }

    @Override
    public @Nullable T remove(Player player) {
        return player.removeData(type::value);
    }

    @Override
    public @Nullable T update(Player player, T value) {
        return player.setData(type::value, value);
    }

    @Override
    public @Nullable T get(Level level) {
        return level.getExistingDataOrNull(type::value);
    }

    @Override
    public T getOrCreate(Level level) {
        return level.getData(type::value);
    }

    @Override
    public boolean has(Level level) {
        return level.hasData(type::value);
    }

    @Override
    public @Nullable T remove(Level level) {
        return level.removeData(type::value);
    }

    @Override
    public @Nullable T update(Level level, T value) {
        return level.setData(type::value, value);
    }

    @Override
    public @Nullable T get(Entity entity) {
        return entity.getExistingDataOrNull(type::value);
    }

    @Override
    public T getOrCreate(Entity entity) {
        return entity.getData(type::value);
    }

    @Override
    public boolean has(Entity entity) {
        return entity.hasData(type::value);
    }

    @Override
    public @Nullable T remove(Entity entity) {
        return entity.removeData(type::value);
    }

    @Override
    public @Nullable T update(Entity entity, T value) {
        return entity.setData(type::value, value);
    }

    @Override
    public @Nullable T get(BlockEntity blockEntity) {
        return blockEntity.getExistingDataOrNull(type::value);
    }

    @Override
    public T getOrCreate(BlockEntity blockEntity) {
        return blockEntity.getData(type::value);
    }

    @Override
    public boolean has(BlockEntity blockEntity) {
        return blockEntity.hasData(type::value);
    }

    @Override
    public @Nullable T remove(BlockEntity blockEntity) {
        return blockEntity.removeData(type::value);
    }

    @Override
    public @Nullable T update(BlockEntity blockEntity, T value) {
        return blockEntity.setData(type::value, value);
    }

    @Override
    public @Nullable T get(ChunkAccess chunk) {
        return chunk.getExistingDataOrNull(type::value);
    }

    @Override
    public T getOrCreate(ChunkAccess chunk) {
        return chunk.getData(type::value);
    }

    @Override
    public boolean has(ChunkAccess chunk) {
        return chunk.hasData(type::value);
    }

    @Override
    public @Nullable T remove(ChunkAccess chunk) {
        return chunk.removeData(type::value);
    }

    @Override
    public @Nullable T update(ChunkAccess chunk, T value) {
        return chunk.setData(type::value, value);
    }
}
