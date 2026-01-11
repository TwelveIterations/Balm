package net.blay09.mods.balm.fabric.client.internal.model.geom;

import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class FabricBalmModelLayerRegistrar implements BalmModelLayerRegistrar {
    public static final FabricBalmModelLayerRegistrar INSTANCE = new FabricBalmModelLayerRegistrar();

    @Override
    public ModelLayerLocation register(Identifier location, String layer, Supplier<LayerDefinition> layerDefinition) {
        final var modelLayerLocation = new ModelLayerLocation(location, layer);
        ModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinition::get);
        return modelLayerLocation;
    }
}
