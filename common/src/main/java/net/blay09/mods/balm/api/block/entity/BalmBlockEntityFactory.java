package net.blay09.mods.balm.api.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface BalmBlockEntityFactory<T extends BlockEntity> {
    T create(BlockPos blockPos, BlockState blockState);
}
