package net.blay09.mods.balm.world.level.biome;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;


public interface BiomePredicate {

    boolean test(Holder<Biome> biomeHolder);

}
