package net.blay09.mods.balm.core;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmRegistrar {
    default <T> Holder<T> register(ResourceKey<T> resourceKey, Supplier<T> resourceSupplier) {
        return register(resourceKey, (id) -> resourceSupplier.get());
    }

    <T> Holder<T> register(ResourceKey<T> resourceKey, Function<Identifier, T> resourceFunction);

    <T> Scoped<T> scoped(ResourceKey<? extends Registry<T>> registryKey, String namespace);

    interface Scoped<T> {
        Holder<T> register(String name, Function<Identifier, T> resourceFunction);
    }
}
