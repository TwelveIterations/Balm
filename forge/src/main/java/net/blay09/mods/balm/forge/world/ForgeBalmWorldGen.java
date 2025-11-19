package net.blay09.mods.balm.forge.world;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.api.world.BiomeModifier;
import net.blay09.mods.balm.api.world.BiomePredicate;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class ForgeBalmWorldGen implements BalmWorldGen {

    public static final MapCodec<BalmBiomeModifier> BALM_BIOME_MODIFIER_CODEC = MapCodec.unit(BalmBiomeModifier.INSTANCE);
    private static final List<Pair<BiomePredicate, BiomeModifier>> biomeModifiers = Collections.synchronizedList(new ArrayList<>());

    public static void initializeBalmBiomeModifiers(BusGroup modEventBus) {
        var registry = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "balm");
        registry.register("balm", () -> BALM_BIOME_MODIFIER_CODEC);
        registry.register(modEventBus);
    }

    @Override
    public <T extends Feature<?>> DeferredObject<T> registerFeature(Identifier identifier, Supplier<T> supplier) {
        final var namespace = identifier.getNamespace();
        final var register = DeferredRegisters.get(ForgeRegistries.FEATURES, namespace);
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public <T extends PlacementModifierType<?>> DeferredObject<T> registerPlacementModifier(Identifier identifier, Supplier<T> supplier) {
        final var register = DeferredRegisters.get(Registries.PLACEMENT_MODIFIER_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public <T extends PoiType> DeferredObject<T> registerPoiType(Identifier identifier, Supplier<T> supplier) {
        final var register = DeferredRegisters.get(Registries.POINT_OF_INTEREST_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public void modifyBiome(Identifier id, BiomePredicate predicate, BiomeModifier modifier) {
        biomeModifiers.add(Pair.of(predicate, modifier));
    }

    public void modifyBiome(Holder<Biome> biome, net.minecraftforge.common.world.BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == net.minecraftforge.common.world.BiomeModifier.Phase.ADD) {
            final var modificationBuilder = new ForgeBiomeModificationBuilder(builder);
            for (var biomeModifierPair : biomeModifiers) {
                if (biomeModifierPair.getFirst().test(biome)) {
                    biomeModifierPair.getSecond().modifyBiome(biome, modificationBuilder);
                }
            }
        }
    }
}
