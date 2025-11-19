package net.blay09.mods.balm.neoforge.world.level.levelgen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.world.level.biome.BiomeModifier;
import net.blay09.mods.balm.world.level.biome.BiomePredicate;
import net.blay09.mods.balm.world.level.levelgen.BalmWorldGen;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeoForgeBalmWorldGen implements BalmWorldGen {

    public static final MapCodec<BalmBiomeModifier> BALM_BIOME_MODIFIER_CODEC = MapCodec.unit(BalmBiomeModifier.INSTANCE);
    private static final List<Pair<BiomePredicate, BiomeModifier>> biomeModifiers = Collections.synchronizedList(new ArrayList<>());

    public static void initializeBalmBiomeModifiers(IEventBus modBus) {
        var registry = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "balm");
        registry.register("balm", () -> BALM_BIOME_MODIFIER_CODEC);
        registry.register(modBus);
    }

    @Override
    public void modifyBiome(Identifier id, BiomePredicate predicate, BiomeModifier modifier) {
        biomeModifiers.add(Pair.of(predicate, modifier));
    }

    public void modifyBiome(Holder<Biome> biome, net.neoforged.neoforge.common.world.BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == net.neoforged.neoforge.common.world.BiomeModifier.Phase.ADD) {
            final var modificationBuilder = new NeoForgeBiomeModificationBuilder(builder);
            for (var biomeModifierPair : biomeModifiers) {
                if (biomeModifierPair.getFirst().test(biome)) {
                    biomeModifierPair.getSecond().modifyBiome(biome, modificationBuilder);
                }
            }
        }
    }
}
