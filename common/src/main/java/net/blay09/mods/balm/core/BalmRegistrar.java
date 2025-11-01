package net.blay09.mods.balm.core;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public interface BalmRegistrar {
    default <T> Holder<T> register(ResourceKey<T> resourceKey, T resource) {
        return register(resourceKey, (Supplier<T>) () -> resource);
    }

    <T> Holder<T> register(ResourceKey<T> resourceKey, Supplier<T> resourceSupplier);

    <T> Scoped<T> scoped(ResourceKey<? extends Registry<T>> registryKey, String namespace);

    interface Scoped<T> {
        default Holder<T> register(String resourcePath, T resource) {
            return register(resourcePath, () -> resource);
        }

        Holder<T> register(String resourcePath, Supplier<T> resourceSupplier);
    }
}
