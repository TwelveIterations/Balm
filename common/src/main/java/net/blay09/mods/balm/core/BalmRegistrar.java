package net.blay09.mods.balm.core;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmRegistrar {
    /**
     * Creates a custom code-driven registry.
     *
     * @param registryKey the registry key to create.
     * @return the created registry.
     */
    default <T> Registry<T> createCustomRegistry(ResourceKey<? extends Registry<T>> registryKey) {
        return createCustomRegistry(registryKey, builder -> {
        });
    }

    /**
     * Creates a custom code-driven defaulted registry.
     *
     * @param registryKey the registry key to create.
     * @return the created registry.
     */
    default <T> Registry<T> createCustomRegistry(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation defaultKey) {
        return createCustomRegistry(registryKey, builder -> {
            builder.defaultKey(defaultKey);
        });
    }

    /**
     * Creates a custom code-driven registry with additional options. Call this during mod initialization, before registering
     * entries to the registry.
     *
     * @param registryKey      the registry key to create.
     * @param builderConsumer callback for configuring the registry.
     * @return the created registry.
     */
    <T> Registry<T> createCustomRegistry(ResourceKey<? extends Registry<T>> registryKey, Consumer<CustomRegistryBuilder<T>> builderConsumer);

    default <T> Holder<T> register(ResourceKey<T> resourceKey, Supplier<T> resourceSupplier) {
        return register(resourceKey, (id) -> resourceSupplier.get());
    }

    <T> Holder<T> register(ResourceKey<T> resourceKey, Function<ResourceLocation, T> resourceFunction);

    <T> Scoped<T> scoped(ResourceKey<? extends Registry<T>> registryKey, String namespace);

    interface Scoped<T> {
        Holder<T> register(String name, Function<ResourceLocation, T> resourceFunction);
    }
}
