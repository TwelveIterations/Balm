package net.blay09.mods.balm.api.world;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.world.level.biome.BiomeModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.function.Supplier;

public interface BalmWorldGen {
    <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation identifier, Supplier<T> supplier);

    <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation identifier, Supplier<T> supplier);

    <T extends PoiType> DeferredObject<T> registerPoiType(ResourceLocation identifier, Supplier<T> supplier);

    void addFeatureToBiomes(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceLocation configuredFeatureIdentifier);

    void modifyBiome(ResourceLocation id, BiomePredicate predicate, BiomeModifier modifier);
}
