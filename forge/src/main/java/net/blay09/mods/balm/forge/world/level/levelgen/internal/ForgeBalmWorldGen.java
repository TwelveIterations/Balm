package net.blay09.mods.balm.forge.world.level.levelgen.internal;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.blay09.mods.balm.forge.world.level.biome.internal.ForgeBiomeModificationBuilder;
import net.blay09.mods.balm.world.level.biome.BiomeModifier;
import net.blay09.mods.balm.world.level.biome.BiomePredicate;
import net.blay09.mods.balm.world.level.levelgen.BalmWorldGen;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForgeBalmWorldGen implements BalmWorldGen {

    public static final MapCodec<BalmBiomeModifier> BALM_BIOME_MODIFIER_CODEC = MapCodec.unit(BalmBiomeModifier.INSTANCE);
    private static final List<Pair<BiomePredicate, BiomeModifier>> biomeModifiers = Collections.synchronizedList(new ArrayList<>());

    public static void initializeBalmBiomeModifiers(BusGroup modEventBus) {
        var registry = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, "balm");
        registry.register("balm", () -> BALM_BIOME_MODIFIER_CODEC);
        registry.register(modEventBus);
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
