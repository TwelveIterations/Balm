package net.blay09.mods.balm.neoforge.client.rendering;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record NeoForgeBalmModels(NamespaceResolver namespaceResolver) implements BalmModels {

    @Override
    public DeferredObject<BakedModel> loadModel(ResourceLocation identifier) {
        final var registrations = getActiveRegistrations();
        final var deferredModel = new DeferredObject<BakedModel>(identifier) {
            @Override
            public BakedModel resolve() {
                return registrations.bakedStandaloneModels.get(identifier);
            }

            @Override
            public boolean canResolve() {
                return registrations.bakedStandaloneModels.containsKey(identifier);
            }
        };
        registrations.additionalModels.add(identifier);
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
        public final List<ResourceLocation> additionalModels = new ArrayList<>();
        public Map<ResourceLocation, BakedModel> bakedStandaloneModels = new HashMap<>();

        @SubscribeEvent
        public void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
            additionalModels.forEach(event::register);
        }

        @SubscribeEvent
        public void onBakingCompleted(ModelEvent.BakingCompleted event) {
            bakedStandaloneModels = event.getBakingResult().standaloneModels();
        }
    }

}
