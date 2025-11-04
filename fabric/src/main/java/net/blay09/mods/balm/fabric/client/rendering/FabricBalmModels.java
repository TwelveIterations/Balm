package net.blay09.mods.balm.fabric.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Deprecated
public final class FabricBalmModels implements BalmModels, ModelLoadingPlugin {
    private record ExtraModelRegistration(ResourceLocation identifier, ExtraModelKey<BlockStateModel> extraModelKey) {
    }

    private final List<ExtraModelRegistration> additionalModels = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void initialize(Context context) {
        for (final var additionalModel : additionalModels) {
            context.addModel(additionalModel.extraModelKey(), new SimpleUnbakedExtraModel<>(additionalModel.identifier(), (model, baker) -> {
                final var textureSlots = model.getTopTextureSlots();
                final var ambientOcclusion = model.getTopAmbientOcclusion();
                final var quadCollection = model.bakeTopGeometry(textureSlots, baker, BlockModelRotation.X0_Y0);
                final var particleSprite = model.resolveParticleSprite(textureSlots, baker);
                return new SingleVariant(new SimpleModelWrapper(quadCollection, ambientOcclusion, particleSprite));
            }));
        }
    }

    @Override
    public DeferredObject<BlockStateModel> loadModel(final ResourceLocation identifier) {
        final var standaloneModelKey = ExtraModelKey.<BlockStateModel>create(identifier::toString);
        final var deferredObject = new DeferredObject<BlockStateModel>(identifier) {
            @Override
            public BlockStateModel resolve() {
                return Minecraft.getInstance().getModelManager().getModel(standaloneModelKey);
            }

            @Override
            public boolean canResolve() {
                final var model = Minecraft.getInstance().getModelManager().getModel(standaloneModelKey);
                return model != null;
            }
        };
        additionalModels.add(new ExtraModelRegistration(identifier, standaloneModelKey));
        return deferredObject;
    }

    @Override
    public BalmModels scoped(String modId) {
        return this;
    }

}
