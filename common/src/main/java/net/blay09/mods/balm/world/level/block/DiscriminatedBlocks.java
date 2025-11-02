package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface DiscriminatedBlocks<T> {
    default DeferredBlock getUndiscriminatedDeferred() {
        return getDeferred(null);
    }

    DeferredBlock getDeferred(@Nullable T discriminator);

    default Block getUndiscriminated() {
        return get(null);
    }

    Block get(@Nullable T discriminator);

    Collection<DeferredBlock> getAllDeferred();

    Collection<Block> getAll();

    Collection<DeferredBlock> getDiscriminatedDeferred();

    Collection<Block> getDiscriminated();

    void forEach(Consumer<Block> consumer);

    void forEachDeferred(Consumer<DeferredBlock> consumer);

    void forEach(BiConsumer<T, Block> consumer);

    void forEachDeferred(BiConsumer<T, DeferredBlock> consumer);

    void forEachDiscriminated(BiConsumer<T, Block> consumer);

    void forEachDiscriminatedDeferred(BiConsumer<T, DeferredBlock> consumer);

    static <T> String prefix(T value, String name) {
        return value == null ? name : name + "_" + value;
    }

    static <T> String suffix(T value, String name) {
        return value == null ? name : value + "_" + name;
    }
}
