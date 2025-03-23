package net.blay09.mods.balm.neoforge.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NeoForgeBalmModels implements BalmModels {

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public DeferredObject<BlockStateModel> loadModel(ResourceLocation identifier) {
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
        getRegistrations(identifier.getNamespace()).additionalModels.add(identifier);
        return deferredModel;
    }

    public void register(String modId, IEventBus eventBus) {
        eventBus.register(getRegistrations(modId));
    }

    private Registrations getRegistrations(String modId) {
        return registrations.computeIfAbsent(modId, it -> new Registrations());
    }

    private static class Registrations {
        public final List<ResourceLocation> additionalModels = new ArrayList<>();

        @SubscribeEvent
        public void onRegisterAdditionalModels(ModelEvent.RegisterStandalone event) {
            for (final var additionalModel : additionalModels) {
                event.register(new StandaloneModelKey<>(additionalModel), (model, baker) -> {
                    // TODO 1.21.5: Additional Models
                    return Minecraft.getInstance().getModelManager().getMissingBlockStateModel();
                });
            }
        }
    }

}
