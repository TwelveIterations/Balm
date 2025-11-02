package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.level.block.Block;

public interface DiscriminatedBlocks<T> {
    DeferredBlock getDeferred(T discriminator);

    Block get(T discriminator);
}
