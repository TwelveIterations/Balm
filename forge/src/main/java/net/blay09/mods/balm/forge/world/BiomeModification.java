package net.blay09.mods.balm.forge.world;

import net.blay09.mods.balm.api.world.BiomePredicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

@Deprecated
class BiomeModification {
    private final BiomePredicate biomePredicate;
    private final GenerationStep.Decoration step;
    private final ResourceKey<PlacedFeature> placedFeatureKey;

    BiomeModification(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeatureKey) {
        this.biomePredicate = biomePredicate;
        this.step = step;
        this.placedFeatureKey = placedFeatureKey;
    }

    public BiomePredicate getBiomePredicate() {
        return biomePredicate;
    }

    public GenerationStep.Decoration getStep() {
        return step;
    }

    public ResourceKey<PlacedFeature> getConfiguredFeatureKey() {
        return placedFeatureKey;
    }
}
