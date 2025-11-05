package net.blay09.mods.balm.api.world;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface BiomeModificationBuilder {
    void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeature);
}
