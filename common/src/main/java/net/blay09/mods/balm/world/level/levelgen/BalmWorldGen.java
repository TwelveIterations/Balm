package net.blay09.mods.balm.world.level.levelgen;

import net.blay09.mods.balm.world.level.biome.BiomeModifier;
import net.blay09.mods.balm.world.level.biome.BiomePredicate;
import net.minecraft.resources.Identifier;

public interface BalmWorldGen {
    void modifyBiome(Identifier id, BiomePredicate predicate, BiomeModifier modifier);
}
