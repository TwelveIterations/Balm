package net.blay09.mods.balm.client.renderer.blockentity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface BalmBlockEntityRendererRegistrar {

    <TBlockEntity extends BlockEntity> void register(Holder<BlockEntityType<TBlockEntity>> blockEntityTypeHolder, BlockEntityRendererProvider<? super TBlockEntity> provider);

    <TBlockEntity extends BlockEntity> void register(String name, Supplier<BlockEntityType<TBlockEntity>> blockEntityTypeSupplier, BlockEntityRendererProvider<? super TBlockEntity> provider);

}
