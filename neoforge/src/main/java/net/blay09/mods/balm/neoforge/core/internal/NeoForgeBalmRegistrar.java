package net.blay09.mods.balm.neoforge.core.internal;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.core.AbstractDynamicRegistryBuilder;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.CustomRegistryBuilder;
import net.blay09.mods.balm.core.AbstractCustomRegistryBuilder;
import net.blay09.mods.balm.core.DynamicRegistryBuilder;
import net.blay09.mods.balm.neoforge.platform.event.internal.ModBusEventRegisters;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Consumer;
import java.util.function.Function;

public class NeoForgeBalmRegistrar implements BalmRegistrar {

    @Override
    public <T> Registry<T> createCustomRegistry(ResourceKey<? extends Registry<T>> registryKey, Consumer<CustomRegistryBuilder<T>> builderConsumer) {
        final var builder = new NeoForgeCustomRegistryBuilder<>(registryKey);
        builderConsumer.accept(builder);
        final var registry = builder.build();
        ModBusEventRegisters.getRegistrations(registryKey.identifier().getNamespace(), NeoForgeCustomRegistryRegistrar.class).add(registry);
        return registry;
    }

    @Override
    public <T> void createDynamicRegistry(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec, Consumer<DynamicRegistryBuilder<T>> builderConsumer) {
        final var builder = new AbstractDynamicRegistryBuilder<T>() {
        };
        builderConsumer.accept(builder);
        ModBusEventRegisters.getRegistrations(registryKey.identifier().getNamespace(), NeoForgeDynamicRegistryRegistrar.class).add(registryKey, codec, builder);
    }

    @Override
    public <T> Holder<T> register(ResourceKey<T> resourceKey, Function<Identifier, T> resourceFunction) {
        final var deferredRegister = DeferredRegisters.get(resourceKey.registryKey(), resourceKey.identifier().getNamespace());
        return deferredRegister.register(resourceKey.identifier().getPath(), () -> resourceFunction.apply(resourceKey.identifier()));
    }

    @Override
    public <T> void addAlias(ResourceKey<? extends Registry<T>> registryKey, Identifier oldId, Identifier newId) {
        DeferredRegisters.get(registryKey, newId.getNamespace()).addAlias(oldId, newId);
    }

    @Override
    public <T> BalmRegistrar.Scoped<T> scoped(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
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
        public Holder<T> register(String name, Function<Identifier, T> resourceFunction) {
            final var deferredRegister = DeferredRegisters.get(registryKey, namespace);
            return deferredRegister.register(name, resourceFunction);
        }

        @Override
        public void addAlias(String oldName, String newName) {
            addAlias(Identifier.fromNamespaceAndPath(namespace, oldName),Identifier.fromNamespaceAndPath(namespace, newName));
        }

        @Override
        public void addAlias(Identifier oldId, Identifier newId) {
            DeferredRegisters.get(registryKey, newId.getNamespace()).addAlias(oldId, newId);
        }
    }

    private static class NeoForgeCustomRegistryBuilder<T> extends AbstractCustomRegistryBuilder<T> {
        private final ResourceKey<? extends Registry<T>> registryKey;

        public NeoForgeCustomRegistryBuilder(ResourceKey<? extends Registry<T>> registryKey) {
            this.registryKey = registryKey;
        }

        public Registry<T> build() {
            final var builder = new RegistryBuilder<T>(registryKey);
            final var defaultKey = getDefaultKey();
            if (defaultKey != null) {
                builder.defaultKey(defaultKey);
            }
            if (shouldSync()) {
                builder.sync(true);
            }
            return builder.create();
        }
    }

}
