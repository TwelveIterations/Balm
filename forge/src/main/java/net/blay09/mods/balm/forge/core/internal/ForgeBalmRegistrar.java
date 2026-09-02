package net.blay09.mods.balm.forge.core.internal;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.blay09.mods.balm.core.CustomRegistryBuilder;
import net.blay09.mods.balm.core.DeferredHolder;
import net.blay09.mods.balm.core.DynamicRegistryBuilder;
import net.blay09.mods.balm.forge.platform.event.internal.ModBusEventRegisters;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;

public class ForgeBalmRegistrar implements BalmRegistrar {

    @Override
    public <T> Registry<T> createCustomRegistry(ResourceKey<? extends Registry<T>> registryKey, Consumer<CustomRegistryBuilder<T>> builderConsumer) {
        throw new UnsupportedOperationException("Custom registries are not yet supported on Forge.");
    }

    @Override
    public <T> void createDynamicRegistry(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec, Consumer<DynamicRegistryBuilder<T>> builderConsumer) {
        throw new UnsupportedOperationException("Dynamic registries are not yet supported on Forge.");
    }

    @Override
    public <T> BalmHolderRegistration<T> register(ResourceKey<T> resourceKey, Function<Identifier, T> resourceFunction) {
        final var deferredRegister = DeferredRegisters.get(resourceKey.registryKey(), resourceKey.identifier().getNamespace());
        deferredRegister.register(resourceKey.identifier().getPath(), () -> resourceFunction.apply(resourceKey.identifier()));
        final var holder = new DeferredHolder<>(resourceKey);
        return () -> holder;
    }


    @Override
    public <T> void addAlias(ResourceKey<? extends Registry<T>> registryKey, Identifier oldId, Identifier newId) {
        ModBusEventRegisters.getRegistrations(newId.getNamespace(), ForgeRegistryAliasRemapper.class).addAlias(registryKey, oldId, newId);
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
        public BalmHolderRegistration<T> register(String name, Function<Identifier, T> resourceFunction) {
            final var deferredRegister = DeferredRegisters.get(registryKey, namespace);
            final var registryObject = deferredRegister.register(name, () -> resourceFunction.apply(Identifier.fromNamespaceAndPath(namespace, name)));
            final var holder = new DeferredHolder<T>(registryObject.getKey());
            return () -> holder;
        }

        @Override
        public void addAlias(String oldName, String newName) {
            addAlias(Identifier.fromNamespaceAndPath(namespace, oldName), Identifier.fromNamespaceAndPath(namespace, newName));
        }

        @Override
        public void addAlias(Identifier oldId, Identifier newId) {
            ModBusEventRegisters.getRegistrations(namespace, ForgeRegistryAliasRemapper.class).addAlias(registryKey, oldId, newId);
        }

    }
}
