package net.blay09.mods.balm.api.world;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

@FunctionalInterface
public interface BiomeModifier {
    void modifyBiome(Holder<Biome> biome, BiomeModificationBuilder builder);
}
