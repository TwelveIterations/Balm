package net.blay09.mods.balm.fabric.core.internal;

import net.blay09.mods.balm.core.CustomRegistryBuilder;
import net.blay09.mods.balm.core.AbstractCustomRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class FabricBalmRegistrar implements BalmRegistrar {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Registry<T> createCustomRegistry(ResourceKey<? extends Registry<T>> registryKey, Consumer<CustomRegistryBuilder<T>> builderConsumer) {
        final var builder = new AbstractCustomRegistryBuilder<T>() {
        };
        builderConsumer.accept(builder);

        final var defaultKey = builder.getDefaultKey();
        final var fabricBuilder = defaultKey != null
                ? FabricRegistryBuilder.createDefaulted((ResourceKey<Registry<T>>) registryKey, defaultKey)
                : FabricRegistryBuilder.createSimple((ResourceKey<Registry<T>>) registryKey);
        if (builder.shouldSync()) {
            fabricBuilder.attribute(RegistryAttribute.SYNCED);
        }

        return fabricBuilder.buildAndRegister();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Holder<T> register(ResourceKey<T> resourceKey, Function<ResourceLocation, T> resourceFunction) {
        final var registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(resourceKey.registry());
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
            final var registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
            Objects.requireNonNull(registry);
            final var resourceKey = ResourceKey.create(registryKey, ResourceLocation.fromNamespaceAndPath(namespace, name));
            return registry.wrapAsHolder(Registry.register(registry, resourceKey, resourceFunction.apply(resourceKey.location())));
        }
    }

}
