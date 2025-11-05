package net.blay09.mods.balm.api.world;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.function.Supplier;

public interface BalmWorldGen {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.Balm#registrar(ResourceKey, String)} instead.
     */
    @Deprecated
    <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation identifier, Supplier<T> supplier);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.Balm#registrar(ResourceKey, String)} instead.
     */
    @Deprecated
    <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation identifier, Supplier<T> supplier);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.Balm#registrar(ResourceKey, String)} instead.
     */
    @Deprecated
    <T extends PoiType> DeferredObject<T> registerPoiType(ResourceLocation identifier, Supplier<T> supplier);

    void modifyBiome(ResourceLocation id, BiomePredicate predicate, BiomeModifier modifier);

    /**
     * @deprecated Use {@link #modifyBiome(ResourceLocation, BiomePredicate, BiomeModifier)} instead.
     */
    @Deprecated
    default void addFeatureToBiomes(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceLocation placedFeatureIdentifier) {
        modifyBiome(placedFeatureIdentifier, biomePredicate, (biome, builder) -> builder.addFeature(step, ResourceKey.create(Registries.PLACED_FEATURE, placedFeatureIdentifier)));
    }
}
