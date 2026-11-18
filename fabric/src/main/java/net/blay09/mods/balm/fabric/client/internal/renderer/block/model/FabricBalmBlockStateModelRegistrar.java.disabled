package net.blay09.mods.balm.fabric.client.internal.renderer.block.model;

import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.balm.client.renderer.block.model.internal.AbstractBalmBlockStateModelRegistrar;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.SingleVariant;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.resources.Identifier;

public class FabricBalmBlockStateModelRegistrar extends AbstractBalmBlockStateModelRegistrar {
    private final ModelLoadingPlugin.Context context;

    public FabricBalmBlockStateModelRegistrar(ModelLoadingPlugin.Context context) {
        this.context = context;
    }

    @Override
    public DeferredBlockStateModel register(Identifier identifier) {
        final var extraModelKey = ExtraModelKey.<BlockStateModel>create(identifier::toString);
        context.addModel(extraModelKey, new SimpleUnbakedExtraModel<>(identifier, (model, baker) -> {
            final var textureSlots = model.getTopTextureSlots();
            final var ambientOcclusion = model.getTopAmbientOcclusion();
            final var quadCollection = model.bakeTopGeometry(textureSlots, baker, BlockModelRotation.IDENTITY);
            final var particleMaterial = model.resolveParticleMaterial(textureSlots, baker);
            return new SingleVariant(new SimpleModelWrapper(quadCollection, ambientOcclusion, particleMaterial));
        }));
        return new FabricDeferredBlockStateModel(extraModelKey);
    }

    public record FabricDeferredBlockStateModel(
            ExtraModelKey<BlockStateModel> extraModelKey) implements DeferredBlockStateModel {
        @Override
        public BlockStateModel asBlockStateModel() {
            final var modelManager = Minecraft.getInstance().getModelManager();
            final var model = modelManager.getModel(extraModelKey);
            return model != null ? model : modelManager.getBlockStateModelSet().missingModel();
        }
    }
}
