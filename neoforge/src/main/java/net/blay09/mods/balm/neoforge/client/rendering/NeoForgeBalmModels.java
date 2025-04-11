package net.blay09.mods.balm.neoforge.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.block.model.SingleVariant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record NeoForgeBalmModels(NamespaceResolver namespaceResolver) implements BalmModels {

    @Override
    public DeferredObject<BlockStateModel> loadModel(ResourceLocation identifier) {
        final var registrations = getActiveRegistrations();
        final var standaloneModelKey = new StandaloneModelKey<BlockStateModel>(identifier);
        final var deferredModel = new DeferredObject<BlockStateModel>(identifier) {
            @Override
            public BlockStateModel resolve() {
                return Minecraft.getInstance().getModelManager().getStandaloneModel(standaloneModelKey);
            }

            @Override
            public boolean canResolve() {
                final var model = Minecraft.getInstance().getModelManager().getStandaloneModel(standaloneModelKey);
                return model != null;
            }
        };
        registrations.additionalModels.add(standaloneModelKey);
        return deferredModel;
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    @Override
    public BalmModels scoped(String modId) {
        return new NeoForgeBalmModels(new StaticNamespaceResolver(modId));
    }

    public static class Registrations {
        public final List<StandaloneModelKey<BlockStateModel>> additionalModels = new ArrayList<>();

        @SubscribeEvent
        public void onRegisterAdditionalModels(ModelEvent.RegisterStandalone event) {
            for (final var additionalModel : additionalModels) {
                event.register(additionalModel, (model, baker) -> {
                    final var textureSlots = model.getTopTextureSlots();
                    final var ambientOcclusion = model.getTopAmbientOcclusion();
                    final var quadCollection = model.bakeTopGeometry(textureSlots, baker, BlockModelRotation.X0_Y0);
                    final var particleSprite = model.resolveParticleSprite(textureSlots, baker);
                    return new SingleVariant(new SimpleModelWrapper(quadCollection, ambientOcclusion, particleSprite));
                });
            }
        }
    }

}
