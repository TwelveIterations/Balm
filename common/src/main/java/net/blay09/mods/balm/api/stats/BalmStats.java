package net.blay09.mods.balm.api.stats;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#customStats(String, java.util.function.Consumer)} instead.
 */
@Deprecated
public interface BalmStats {
    default void registerCustomStat(Identifier identifier) {
        Balm.getRuntime().customStats(identifier.getNamespace(), registrar -> registrar.register(identifier, StatFormatter.DEFAULT));
    }

    BalmStats LEGACY = new BalmStats() {
    };
}
