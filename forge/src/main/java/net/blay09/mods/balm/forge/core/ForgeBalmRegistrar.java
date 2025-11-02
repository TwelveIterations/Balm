package net.blay09.mods.balm.forge.core;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.DeferredHolder;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class ForgeBalmRegistrar implements BalmRegistrar {

    @Override
    public <T> Holder<T> register(ResourceKey<T> resourceKey, Supplier<T> resourceSupplier) {
        final var deferredRegister = DeferredRegisters.get(resourceKey.registryKey(), resourceKey.location().getNamespace());
        deferredRegister.register(resourceKey.location().getPath(), resourceSupplier);
        return new DeferredHolder<>(resourceKey);
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
        public Holder<T> register(String resourcePath, Supplier<T> resourceSupplier) {
            final var deferredRegister = DeferredRegisters.get(registryKey, namespace);
            final var registryObject = deferredRegister.register(resourcePath, resourceSupplier);
            return registryObject.getHolder().orElseThrow();
        }
    }
}
