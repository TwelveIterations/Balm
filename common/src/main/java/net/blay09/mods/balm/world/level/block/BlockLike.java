package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockLike extends ItemLike {
    Block asBlock();

    BlockState defaultBlockState();
}
