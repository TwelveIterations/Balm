package net.blay09.mods.balm.fabric.client.internal.renderer.block.model;

import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.balm.client.renderer.block.model.internal.AbstractBalmBlockStateModelRegistrar;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class FabricBalmBlockStateModelRegistrar extends AbstractBalmBlockStateModelRegistrar {
    private final ModelLoadingPlugin.Context context;

    public FabricBalmBlockStateModelRegistrar(ModelLoadingPlugin.Context context) {
        this.context = context;
    }

    @Override
    public DeferredBlockStateModel register(ResourceLocation identifier) {
        final var extraModelKey = new ModelResourceLocation(identifier, "fabric_resource");
        context.addModels(identifier);
        return new FabricDeferredBlockStateModel(extraModelKey);
    }

    public record FabricDeferredBlockStateModel(ModelResourceLocation extraModelKey) implements DeferredBlockStateModel {
        @Override
        public BakedModel asBlockStateModel() {
            return Minecraft.getInstance().getModelManager().getModel(extraModelKey);
        }
    }
}
