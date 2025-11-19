package net.blay09.mods.balm.client.model.geom;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public interface BalmModelLayerRegistrar {

    default ModelLayerLocation register(Identifier location, Supplier<LayerDefinition> layerDefinition) {
        return register(location, "main", layerDefinition);
    }

    ModelLayerLocation register(Identifier location, String layer, Supplier<LayerDefinition> layerDefinition);
}
