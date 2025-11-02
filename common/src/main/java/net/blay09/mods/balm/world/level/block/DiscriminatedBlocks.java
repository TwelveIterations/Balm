package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.level.block.Block;

import java.util.Collection;

public interface DiscriminatedBlocks<T> {
    DeferredBlock getDeferred(T discriminator);

    Block get(T discriminator);

    Collection<DeferredBlock> allDeferred();

    Collection<Block> all();
}
