package net.blay09.mods.balm.core;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public interface BalmHolderRegistration<T> {

    default ResourceKey<T> asResourceKey() {
        return asHolder().unwrapKey().orElseThrow();
    }

    default Supplier<T> asSupplier() {
        return asHolder()::value;
    }

    Holder<T> asHolder();

}
