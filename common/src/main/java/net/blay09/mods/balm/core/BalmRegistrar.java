package net.blay09.mods.balm.core;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmRegistrar {
    default <T> Holder<T> register(ResourceKey<T> resourceKey, Supplier<T> resourceSupplier) {
        return register(resourceKey, (Function<ResourceLocation, T>) (identifier) -> resourceSupplier.get());
    }

    <T> Holder<T> register(ResourceKey<T> resourceKey, Function<ResourceLocation, T> resourceFunction);

    <T> Scoped<T> scoped(ResourceKey<? extends Registry<T>> registryKey, String namespace);

    interface Scoped<T> {
        default Holder<T> register(String name, Supplier<T> resourceSupplier) {
            return register(name, (identifier) -> resourceSupplier.get());
        }

        Holder<T> register(String name, Function<ResourceLocation, T> resourceFunction);
    }
}
