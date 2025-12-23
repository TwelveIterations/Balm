package net.blay09.mods.balm.fabric.client.rendering;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FabricModelLayers {
    private static final Map<ModelLayerLocation, Supplier<LayerDefinition>> layerDefinitions = new ConcurrentHashMap<>();

    public static Map<ModelLayerLocation, LayerDefinition> createRoots() {
        return layerDefinitions.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, it -> it.getValue().get()));
    }

    public static void register(ModelLayerLocation modelLayerLocation, Supplier<LayerDefinition> layerDefinition) {
        layerDefinitions.put(modelLayerLocation, layerDefinition);
    }
}
