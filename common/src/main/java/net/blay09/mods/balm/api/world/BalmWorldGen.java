package net.blay09.mods.balm.api.world;

import net.minecraft.resources.Identifier;

public interface BalmWorldGen {
    void modifyBiome(Identifier id, BiomePredicate predicate, BiomeModifier modifier);
}
