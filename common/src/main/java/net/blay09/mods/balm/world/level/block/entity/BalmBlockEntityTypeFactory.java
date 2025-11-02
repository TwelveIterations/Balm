package net.blay09.mods.balm.world.level.block.entity;

import net.blay09.mods.balm.world.level.block.BlockLike;
import net.blay09.mods.balm.world.level.block.DiscriminatedBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.function.Supplier;

public interface BalmBlockEntityTypeFactory {
    <T extends BlockEntity> BalmBlockEntityTypeRegistration<T> register(String name, BlockEntitySupplier<T> constructor, BlockLike... blocks);

    <T extends BlockEntity> BalmBlockEntityTypeRegistration<T> register(String name, BlockEntitySupplier<T> constructor, DiscriminatedBlocks<?> blocks);

    <T extends BlockEntity> BalmBlockEntityTypeRegistration<T> register(String name, BlockEntitySupplier<T> constructor, Supplier<Set<Block>> blocksSupplier);

    <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BlockEntitySupplier<T> constructor, Set<Block> blocks);

    @FunctionalInterface
    interface BlockEntitySupplier<T extends BlockEntity> {
        T create(BlockPos pos, BlockState state);
    }
}
