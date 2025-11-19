package net.blay09.mods.balm.api.world;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;


public interface BiomePredicate {

    boolean test(Holder<Biome> biomeHolder);

}
