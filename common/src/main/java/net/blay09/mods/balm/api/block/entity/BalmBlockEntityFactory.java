package net.blay09.mods.balm.api.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#blockEntityTypes(String, Consumer)} instead.
 */
@Deprecated
public interface BalmBlockEntityFactory<T extends BlockEntity> {
    T create(BlockPos blockPos, BlockState blockState);
}
