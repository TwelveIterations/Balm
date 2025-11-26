package net.blay09.mods.balm.api.stats;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#customStats(Consumer)} instead.
 */
@Deprecated
public interface BalmStats {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#customStats(Consumer)} instead.
     */
    @Deprecated
    void registerCustomStat(ResourceLocation identifier);
}
