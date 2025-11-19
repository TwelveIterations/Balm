package net.blay09.mods.balm.forge.client.model.geom;

import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.event.EntityRenderersEvent;

import java.util.function.Supplier;

public class ForgeBalmModelLayerRegistrar implements BalmModelLayerRegistrar {
    private final EntityRenderersEvent.RegisterLayerDefinitions event;

    public ForgeBalmModelLayerRegistrar(EntityRenderersEvent.RegisterLayerDefinitions event) {
        this.event = event;
    }

    @Override
    public ModelLayerLocation register(Identifier location, String layer, Supplier<LayerDefinition> layerDefinition) {
        final var modelLayerLocation = new ModelLayerLocation(location, layer);
        event.registerLayerDefinition(modelLayerLocation, layerDefinition);
        return modelLayerLocation;
    }
}
