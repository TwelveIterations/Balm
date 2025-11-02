package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.level.block.Block;

import java.util.Collection;

public interface DiscriminatedBlocks<T> {
    DeferredBlock getDeferred(T discriminator);

    Block get(T discriminator);

    Collection<DeferredBlock> getAllDeferred();

    Collection<Block> getAll();

    Collection<DeferredBlock> getDiscriminatedDeferred();

    Collection<Block> getDiscriminated();

    static <T> String prefix(T value, String name) {
        return value == null ? name : name + "_" + value;
    }

    static <T> String suffix(T value, String name) {
        return value == null ? name : value + "_" + name;
    }
}
