package net.blay09.mods.balm.fabric.core.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.Objects;
import java.util.function.Function;

public class FabricBalmRegistrar implements BalmRegistrar {

    private static Registry<?> getRawRegistry(ResourceKey<? extends Registry<?>> registryKey) {
        return Objects.requireNonNull(BuiltInRegistries.REGISTRY.getValue(registryKey.identifier()));
    }

    @SuppressWarnings("unchecked")
    private static <T> Registry<T> getRegistry(ResourceKey<? extends Registry<T>> registryKey) {
        return (Registry<T>) getRawRegistry(registryKey);
    }

    @Override
    public <T> Holder<T> register(ResourceKey<T> resourceKey, Function<Identifier, T> resourceFunction) {
        final var registry = getRegistry(resourceKey.registryKey());
        return registry.wrapAsHolder(Registry.register(registry, resourceKey, resourceFunction.apply(resourceKey.identifier())));
    }

    @Override
    public <T> Scoped<T> scoped(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        return new Scoped<>(registryKey, namespace);
    }

    @Override
    public <T> void addAlias(ResourceKey<? extends Registry<T>> registryKey, Identifier oldId, Identifier newId) {
        getRegistry(registryKey).addAlias(oldId, newId);
    }

    public static class Scoped<T> implements BalmRegistrar.Scoped<T> {

        private final ResourceKey<? extends Registry<T>> registryKey;
        private final String namespace;

        public Scoped(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
            this.registryKey = registryKey;
            this.namespace = namespace;
        }

        @Override
        public Holder<T> register(String name, Function<Identifier, T> resourceFunction) {
            final var registry = getRegistry(registryKey);
            final var resourceKey = ResourceKey.create(registryKey, Identifier.fromNamespaceAndPath(namespace, name));
            return registry.wrapAsHolder(Registry.register(registry, resourceKey, resourceFunction.apply(resourceKey.identifier())));
        }

        @Override
        public void addAlias(String oldName, String newName) {
            getRegistry(registryKey).addAlias(
                    Identifier.fromNamespaceAndPath(namespace, oldName),
                    Identifier.fromNamespaceAndPath(namespace, newName)
            );
        }
    }

}
