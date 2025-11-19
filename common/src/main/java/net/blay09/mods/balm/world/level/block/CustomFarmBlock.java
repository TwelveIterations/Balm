package net.blay09.mods.balm.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface CustomFarmBlock {
    boolean canSustainPlant(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction, Block plant);

    boolean isFertile(BlockState state, BlockGetter blockGetter, BlockPos pos);
}
