package net.blay09.mods.balm.fabric.client.renderer.block.model;

import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.balm.client.renderer.block.model.AbstractBalmBlockStateModelRegistrar;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.resources.ResourceLocation;

public class FabricBalmBlockStateModelRegistrar extends AbstractBalmBlockStateModelRegistrar {
    private final ModelLoadingPlugin.Context context;

    public FabricBalmBlockStateModelRegistrar(ModelLoadingPlugin.Context context) {
        this.context = context;
    }

    @Override
    public DeferredBlockStateModel register(ResourceLocation identifier) {
        final var extraModelKey = ExtraModelKey.<BlockStateModel>create(identifier::toString);
        context.addModel(extraModelKey, new SimpleUnbakedExtraModel<>(identifier, (model, baker) -> {
            final var textureSlots = model.getTopTextureSlots();
            final var ambientOcclusion = model.getTopAmbientOcclusion();
            final var quadCollection = model.bakeTopGeometry(textureSlots, baker, BlockModelRotation.X0_Y0);
            final var particleSprite = model.resolveParticleSprite(textureSlots, baker);
            return new SingleVariant(new SimpleModelWrapper(quadCollection, ambientOcclusion, particleSprite));
        }));
        return new FabricDeferredBlockStateModel(extraModelKey);
    }

    public record FabricDeferredBlockStateModel(ExtraModelKey<BlockStateModel> extraModelKey) implements DeferredBlockStateModel {
        @Override
        public BlockStateModel asBlockStateModel() {
            return Minecraft.getInstance().getModelManager().getModel(extraModelKey);
        }
    }
}
