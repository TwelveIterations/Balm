package net.blay09.mods.balm.api.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;

import java.util.Collections;
import java.util.List;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.capability.BalmCapabilities} instead.
 */
@Deprecated(forRemoval = true, since = "1.21.5")
public interface BalmProviderHolder {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.capability.BalmCapabilities} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default List<BalmProvider<?>> getProviders() {
        return Collections.emptyList();
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.capability.BalmCapabilities} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
        return Collections.emptyList();
    }
}
