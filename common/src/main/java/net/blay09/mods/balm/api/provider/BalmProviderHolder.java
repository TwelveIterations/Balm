package net.blay09.mods.balm.api.provider;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;

import java.util.Collections;
import java.util.List;

public interface BalmProviderHolder {
    default List<BalmProvider<?>> getProviders() {
        return Collections.emptyList();
    }

    default List<Pair<Direction, BalmProvider<?>>> getSidedProviders() {
        return Collections.emptyList();
    }
}
