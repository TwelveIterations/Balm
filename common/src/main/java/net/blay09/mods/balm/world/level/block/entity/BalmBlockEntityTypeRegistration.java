package net.blay09.mods.balm.world.level.block.entity;

import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface BalmBlockEntityTypeRegistration<T extends BlockEntity> extends BalmHolderRegistration<BlockEntityType<T>> {
    Supplier<BlockEntityType<T>> asSupplier();
}
