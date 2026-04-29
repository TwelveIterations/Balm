package net.blay09.mods.balm.client.renderer.block.model.internal;

import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.resources.Identifier;

public class MissingBalmBlockStateModelRegistrar extends AbstractBalmBlockStateModelRegistrar implements DeferredBlockStateModel {
    @Override
    public DeferredBlockStateModel register(Identifier identifier) {
        return this;
    }

    @Override
    public BlockStateModel asBlockStateModel() {
        return Minecraft.getInstance().getModelManager().getBlockStateModelSet().missingModel();
    }
}
