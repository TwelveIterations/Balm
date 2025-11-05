package net.blay09.mods.balm.forge.client.renderer.model;

import net.blay09.mods.balm.client.renderer.model.BalmModelLayerRegistrar;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

public class ForgeBalmModelLayerRegistrar implements BalmModelLayerRegistrar {
    private final EntityRenderersEvent.RegisterLayerDefinitions event;

    public ForgeBalmModelLayerRegistrar(EntityRenderersEvent.RegisterLayerDefinitions event) {
        this.event = event;
    }

    @Override
    public ModelLayerLocation register(ResourceLocation location, String layer, Supplier<LayerDefinition> layerDefinition) {
        final var modelLayerLocation = new ModelLayerLocation(location, layer);
        event.registerLayerDefinition(modelLayerLocation, layerDefinition);
        return modelLayerLocation;
    }
}
