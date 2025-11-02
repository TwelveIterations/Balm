package net.blay09.mods.balm.core;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

public interface BalmHolderRegistration<T> {

    default ResourceKey<T> asResourceKey() {
        return asHolder().unwrapKey().orElseThrow();
    }

    Holder<T> asHolder();
}
