package net.blay09.mods.balm.api.world;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;


public interface BiomePredicate {

    default boolean test(Holder<Biome> biomeHolder) {
        return test(biomeHolder.unwrapKey().map(ResourceKey::identifier).orElse(null), biomeHolder);
    }

    /**
     * @deprecated Use {@link BiomePredicate#test(Holder<Biome>)} instead.
     */
    @Deprecated
    default boolean test(Identifier key, Holder<Biome> biomeHolder) {
        throw new UnsupportedOperationException("You must implement BiomePredicate.test(Holder<Biome>) if you omit BiomePredicate.test(Identifier, Holder<Biome>)");
    }
}
