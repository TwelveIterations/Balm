package net.blay09.mods.balm.neoforge.core.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.function.Function;

public class NeoForgeBalmRegistrar implements BalmRegistrar {

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
            final var deferredRegister = DeferredRegisters.get(registryKey, namespace);
            deferredRegister.addAlias(
                    Identifier.fromNamespaceAndPath(namespace, oldName),
                    Identifier.fromNamespaceAndPath(namespace, newName)
            );
        }
    }

}
