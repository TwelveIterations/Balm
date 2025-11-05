package net.blay09.mods.balm.fabric.client.model.geom;

import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricBalmModelLayerRegistrar implements BalmModelLayerRegistrar {
    public static final FabricBalmModelLayerRegistrar INSTANCE = new FabricBalmModelLayerRegistrar();

    @Override
    public ModelLayerLocation register(ResourceLocation location, String layer, Supplier<LayerDefinition> layerDefinition) {
        final var modelLayerLocation = new ModelLayerLocation(location, layer);
        EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinition::get);
        return modelLayerLocation;
    }
}
