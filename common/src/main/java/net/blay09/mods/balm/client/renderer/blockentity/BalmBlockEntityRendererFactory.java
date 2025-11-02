package net.blay09.mods.balm.client.renderer.blockentity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface BalmBlockEntityRendererFactory {

    <TBlockEntity extends BlockEntity, TBlockEntityRenderState extends BlockEntityRenderState> void register(Holder<BlockEntityType<TBlockEntity>> blockEntityTypeHolder, BlockEntityRendererProvider<? super TBlockEntity, ? super TBlockEntityRenderState> provider);

    <TBlockEntity extends BlockEntity, TBlockEntityRenderState extends BlockEntityRenderState> void register(String name, Supplier<BlockEntityType<TBlockEntity>> blockEntityTypeSupplier, BlockEntityRendererProvider<? super TBlockEntity, ? super TBlockEntityRenderState> provider);

}
