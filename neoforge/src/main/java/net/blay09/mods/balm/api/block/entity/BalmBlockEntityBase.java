package net.blay09.mods.balm.api.block.entity;

import net.blay09.mods.balm.api.capability.CapabilityType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class BalmBlockEntityBase extends BlockEntity {

    public BalmBlockEntityBase(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.capability.BalmCapabilities#registerProvider(ResourceLocation, CapabilityType, BiFunction, Supplier)} instead.
     */
    @Deprecated(since = "1.21.5")
    protected abstract void buildProviders(List<Object> providers);

}
