package net.blay09.mods.balm.client.renderer.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface BalmModelLayerRegistrar {

    default ModelLayerLocation register(ResourceLocation location, Supplier<LayerDefinition> layerDefinition) {
        return register(location, "main", layerDefinition);
    }

    ModelLayerLocation register(ResourceLocation location, String layer, Supplier<LayerDefinition> layerDefinition);
}
