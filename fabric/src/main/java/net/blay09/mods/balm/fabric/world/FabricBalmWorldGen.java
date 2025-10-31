package net.blay09.mods.balm.fabric.world;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomeModificationBuilder;
import net.blay09.mods.balm.api.world.BiomeModifier;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.blay09.mods.balm.mixin.PoiTypesAccessor;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.function.Supplier;

public class FabricBalmWorldGen implements BalmWorldGen {
    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation identifier, Supplier<T> supplier) {
        return new DeferredObject<>(identifier, () -> {
            T feature = supplier.get();
            Registry.register(BuiltInRegistries.FEATURE, identifier, feature);
            return feature;
        }).resolveImmediately();
    }

    @Override
    public <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation identifier, Supplier<T> supplier) {
        return new DeferredObject<>(identifier, () -> {
            T placementModifierType = supplier.get();
            Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, identifier, placementModifierType);
            return placementModifierType;
        }).resolveImmediately();
    }

    @Override
    public <T extends PoiType> DeferredObject<T> registerPoiType(ResourceLocation identifier, Supplier<T> supplier) {
        return new DeferredObject<>(identifier, () -> {
            T poiType = supplier.get();
            final var resourceKey = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, identifier);
            final var registry = BuiltInRegistries.POINT_OF_INTEREST_TYPE;
            Registry.register(registry, resourceKey, poiType);
            PoiTypesAccessor.callRegisterBlockStates(registry.getOrThrow(resourceKey), poiType.matchingStates());
            return poiType;
        }).resolveImmediately();
    }

    public record FabricBiomeModificationBuilder(RegistryAccess registryAccess,
                                                 BiomeModificationContext builder) implements BiomeModificationBuilder {
        @Override
        public void addFeature(GenerationStep.Decoration step, Holder<PlacedFeature> placedFeature) {
            placedFeature.unwrapKey().ifPresent(it -> builder.getGenerationSettings().addFeature(step, it));
        }
    }

    @Override
    public void modifyBiome(ResourceLocation id, BiomePredicate predicate, BiomeModifier modifier) {
        BiomeModifications.create(id)
                .add(ModificationPhase.ADDITIONS, it -> predicate.test(it.getBiomeRegistryEntry()), (selectionContext, modificationContext) -> {
                    final var registryAccess = Balm.getHooks().getServer().registryAccess();
                    modifier.modifyBiome(selectionContext.getBiomeRegistryEntry(), new FabricBiomeModificationBuilder(registryAccess, modificationContext));
                });
    }
}
