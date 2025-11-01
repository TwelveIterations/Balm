package net.blay09.mods.balm.neoforge.core;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class NeoForgeBalmRegistrar implements BalmRegistrar {

    @Override
    public <T> Holder<T> register(ResourceKey<T> resourceKey, Supplier<T> resource) {
        final var deferredRegister = DeferredRegisters.get(resourceKey.registryKey(), resourceKey.location().getNamespace());
        return deferredRegister.register(resourceKey.location().getPath(), resource);
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
        public Holder<T> register(String resourcePath, Supplier<T> resourceSupplier) {
            final var deferredRegister = DeferredRegisters.get(registryKey, namespace);
            return deferredRegister.register(resourcePath, resourceSupplier);
        }
    }

}
