package net.blay09.mods.balm.neoforge.client.renderer.blockentity;

import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

public class NeoForgeBalmBlockEntityRendererRegistrar implements BalmBlockEntityRendererRegistrar {
    private final EntityRenderersEvent.RegisterRenderers event;

    public NeoForgeBalmBlockEntityRendererRegistrar(EntityRenderersEvent.RegisterRenderers event) {
        this.event = event;
    }

    @Override
    public <TBlockEntity extends BlockEntity, TBlockEntityRenderState extends BlockEntityRenderState> void register(Holder<BlockEntityType<TBlockEntity>> blockEntityTypeHolder, BlockEntityRendererProvider<? super TBlockEntity, ? super TBlockEntityRenderState> provider) {
        event.registerBlockEntityRenderer(blockEntityTypeHolder.value(), provider);
    }

    @Override
    public <TBlockEntity extends BlockEntity, TBlockEntityRenderState extends BlockEntityRenderState> void register(String name, Supplier<BlockEntityType<TBlockEntity>> blockEntityTypeSupplier, BlockEntityRendererProvider<? super TBlockEntity, ? super TBlockEntityRenderState> provider) {
        event.registerBlockEntityRenderer(blockEntityTypeSupplier.get(), provider);
    }
}
