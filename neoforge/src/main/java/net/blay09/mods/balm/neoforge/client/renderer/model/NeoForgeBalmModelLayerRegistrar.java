package net.blay09.mods.balm.neoforge.client.renderer.model;

import net.blay09.mods.balm.client.renderer.model.BalmModelLayerRegistrar;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

public class NeoForgeBalmModelLayerRegistrar implements BalmModelLayerRegistrar {
    private final EntityRenderersEvent.RegisterLayerDefinitions event;

    public NeoForgeBalmModelLayerRegistrar(EntityRenderersEvent.RegisterLayerDefinitions event) {
        this.event = event;
    }

    @Override
    public ModelLayerLocation register(ResourceLocation location, String layer, Supplier<LayerDefinition> layerDefinition) {
        final var modelLayerLocation = new ModelLayerLocation(location, layer);
        event.registerLayerDefinition(modelLayerLocation, layerDefinition);
        return modelLayerLocation;
    }
}
