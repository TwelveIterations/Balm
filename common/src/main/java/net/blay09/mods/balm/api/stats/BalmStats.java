package net.blay09.mods.balm.api.stats;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.minecraft.resources.ResourceLocation;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.Balm#customStats(String, java.util.function.Consumer)} instead.
 */
@Deprecated
public interface BalmStats {
    default void registerCustomStat(ResourceLocation identifier) {
        Balm.getRuntime().customStats(identifier.getNamespace()).register(identifier.getPath());
    }

    BalmStats LEGACY = new BalmStats() {
    };
}
