package net.blay09.mods.balm.neoforge.client.model.geom;

import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

public class NeoForgeBalmModelLayerRegistrar implements BalmModelLayerRegistrar {
    private final EntityRenderersEvent.RegisterLayerDefinitions event;

    public NeoForgeBalmModelLayerRegistrar(EntityRenderersEvent.RegisterLayerDefinitions event) {
        this.event = event;
    }

    @Override
    public ModelLayerLocation register(Identifier location, String layer, Supplier<LayerDefinition> layerDefinition) {
        final var modelLayerLocation = new ModelLayerLocation(location, layer);
        event.registerLayerDefinition(modelLayerLocation, layerDefinition);
        return modelLayerLocation;
    }
}
