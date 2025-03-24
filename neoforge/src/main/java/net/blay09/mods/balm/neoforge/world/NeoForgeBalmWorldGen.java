package net.blay09.mods.balm.neoforge.world;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class NeoForgeBalmWorldGen implements BalmWorldGen {

    public static final MapCodec<BalmBiomeModifier> BALM_BIOME_MODIFIER_CODEC = MapCodec.unit(BalmBiomeModifier.INSTANCE);
    private static final List<BiomeModification> biomeModifications = new ArrayList<>();

    public static void initializeBalmBiomeModifiers(IEventBus modBus) {
        var registry = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "balm");
        registry.register("balm", () -> BALM_BIOME_MODIFIER_CODEC);
        registry.register(modBus);
    }

    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(ResourceLocation identifier, Supplier<T> supplier) {
        final var register = DeferredRegisters.get(Registries.FEATURE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }

    @Override
    public <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(ResourceLocation identifier, Supplier<T> supplier) {
        final var register = DeferredRegisters.get(Registries.PLACEMENT_MODIFIER_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }

    @Override
    public <T extends PoiType> DeferredObject<T> registerPoiType(ResourceLocation identifier, Supplier<T> supplier) {
        final var register = DeferredRegisters.get(Registries.POINT_OF_INTEREST_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }

    @Override
    public void addFeatureToBiomes(BiomePredicate biomePredicate, GenerationStep.Decoration step, ResourceLocation placedFeatureIdentifier) {
        ResourceKey<PlacedFeature> resourceKey = ResourceKey.create(Registries.PLACED_FEATURE, placedFeatureIdentifier);
        biomeModifications.add(new BiomeModification(biomePredicate, step, resourceKey));
    }

    public void modifyBiome(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == BiomeModifier.Phase.ADD) {
            for (var biomeModification : biomeModifications) {
                ResourceLocation location = biome.unwrapKey().map(ResourceKey::location).orElse(null);
                if (location != null && biomeModification.getBiomePredicate().test(location, biome)) {
                    Registry<PlacedFeature> placedFeatures = ServerLifecycleHooks.getCurrentServer()
                            .registryAccess()
                            .registryOrThrow(Registries.PLACED_FEATURE);
                    placedFeatures.getHolder(biomeModification.getConfiguredFeatureKey())
                            .ifPresent(placedFeature -> builder.getGenerationSettings().addFeature(biomeModification.getStep(), placedFeature));
                }
            }
        }
    }
}
