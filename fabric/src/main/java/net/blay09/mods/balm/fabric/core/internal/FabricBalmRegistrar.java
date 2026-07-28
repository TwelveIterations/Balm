package net.blay09.mods.balm.fabric.core.internal;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.core.*;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
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
    public <T> void createDynamicRegistry(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec, Consumer<DynamicRegistryBuilder<T>> builderConsumer) {
        final var builder = new AbstractDynamicRegistryBuilder<T>() {
        };
        builderConsumer.accept(builder);

        if (builder.shouldSync()) {
            final var networkCodec = builder.getNetworkCodec();
            final var options = builder.shouldSkipSyncWhenEmpty()
                    ? new DynamicRegistries.SyncOption[]{DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY}
                    : new DynamicRegistries.SyncOption[0];
            if (networkCodec != null) {
                DynamicRegistries.registerSynced(registryKey, codec, networkCodec, options);
            } else {
                DynamicRegistries.registerSynced(registryKey, codec, options);
            }
        } else {
            DynamicRegistries.register(registryKey, codec);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Holder<T> register(ResourceKey<T> resourceKey, Function<ResourceLocation, T> resourceFunction) {
        final var registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(resourceKey.registry());
        Objects.requireNonNull(registry);
        return registry.wrapAsHolder(Registry.register(registry, resourceKey, resourceFunction.apply(resourceKey.location())));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void addAlias(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation oldId, ResourceLocation newId) {
        final var registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
        Objects.requireNonNull(registry);
        registry.addAlias(oldId, newId);
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

        @Override
        public void addAlias(String oldName, String newName) {
            addAlias(ResourceLocation.fromNamespaceAndPath(namespace, oldName), ResourceLocation.fromNamespaceAndPath(namespace, newName));
        }

        @Override
        @SuppressWarnings("unchecked")
        public void addAlias(ResourceLocation oldId, ResourceLocation newId) {
            final var registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
            Objects.requireNonNull(registry);
            registry.addAlias(oldId, newId);
        }
    }

}
