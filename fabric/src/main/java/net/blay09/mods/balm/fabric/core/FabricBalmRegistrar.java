package net.blay09.mods.balm.fabric.core;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricBalmRegistrar implements BalmRegistrar {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Holder<T> register(ResourceKey<T> resourceKey, Function<ResourceLocation, T> resourceFunction) {
        final var registry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(resourceKey.registry());
        Objects.requireNonNull(registry);
        return registry.wrapAsHolder(Registry.register(registry, resourceKey, resourceFunction.apply(resourceKey.location())));
    }

    @Override
    public <T> Scoped<T> scoped(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        return new Scoped<>(registryKey, namespace);
    }

    public static class Scoped<T> implements BalmRegistrar.Scoped<T> {

        private final ResourceKey<? extends Registry<T>> registryKey;
        private final String namespace;

        public Scoped(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
            this.registryKey = registryKey;
            this.namespace = namespace;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Holder<T> register(String name, Function<ResourceLocation, T> resourceFunction) {
            final var registry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(registryKey.location());
            Objects.requireNonNull(registry);
            final var resourceKey = ResourceKey.create(registryKey, ResourceLocation.fromNamespaceAndPath(namespace, name));
            return registry.wrapAsHolder(Registry.register(registry, resourceKey, resourceFunction.apply(resourceKey.location())));
        }
    }

}
