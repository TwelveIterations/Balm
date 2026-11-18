package net.blay09.mods.balm.fabric.client.internal.renderer.blockentity;

import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.minecraft.client.core.registries.BuiltInClientRegistries;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.core.WritableRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricBalmBlockEntityRendererRegistrar implements BalmBlockEntityRendererRegistrar {

    public static final BalmBlockEntityRendererRegistrar INSTANCE = new FabricBalmBlockEntityRendererRegistrar();

    @Override
    public <TBlockEntity extends BlockEntity, TBlockEntityRenderState extends BlockEntityRenderState> void register(Holder<BlockEntityType<TBlockEntity>> blockEntityTypeHolder, BlockEntityRendererProvider<? super TBlockEntity, ? super TBlockEntityRenderState> provider) {
        BlockEntityRenderers.register((WritableRegistry<BlockEntityRendererProvider<?, ?>>) BuiltInClientRegistries.BLOCK_ENTITY_RENDERER, blockEntityTypeHolder.value(), provider);
    }

    @Override
    public <TBlockEntity extends BlockEntity, TBlockEntityRenderState extends BlockEntityRenderState> void register(String name, Supplier<BlockEntityType<TBlockEntity>> blockEntityTypeSupplier, BlockEntityRendererProvider<? super TBlockEntity, ? super TBlockEntityRenderState> provider) {
        BlockEntityRenderers.register((WritableRegistry<BlockEntityRendererProvider<?, ?>>) BuiltInClientRegistries.BLOCK_ENTITY_RENDERER, blockEntityTypeSupplier.get(), provider);
    }
}
