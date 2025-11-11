package net.blay09.mods.balm.forge.core;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.DeferredHolder;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryManager;

import java.util.Set;
import java.util.function.Function;

public class ForgeBalmRegistrar implements BalmRegistrar {

    private static final Set<ResourceKey<? extends Registry<?>>> earlyConstructRegistries = Set.of(
            Registries.DATA_COMPONENT_TYPE,
            Registries.BLOCK,
            Registries.ITEM,
            Registries.MENU,
            Registries.ENTITY_TYPE
    );

    @Override
    public <T> Holder<T> register(ResourceKey<T> resourceKey, Function<ResourceLocation, T> resourceFunction) {
        // Forge runs all client-side block/item/menu/etc. registration events before the objects themselves are actually registered....
        // So we can't delay construction until registration time for those registries, even though the Supplier of DeferredRegister would encourage it..
        final var constructEarly = earlyConstructRegistries.contains(resourceKey.registryKey());
        final var deferredRegister = DeferredRegisters.get(resourceKey.registryKey(), resourceKey.location().getNamespace());
        final var earlyConstruct = constructEarly ? resourceFunction.apply(resourceKey.location()) : null;
        if (earlyConstruct != null) {
            deferredRegister.register(resourceKey.location().getPath(), () -> earlyConstruct);
            final var registry = RegistryManager.ACTIVE.getRegistry(resourceKey.registryKey());
            return new DeferredHolder<>(resourceKey, registry != null ? registry.getDelegateOrThrow(earlyConstruct) : null);
        } else {
            deferredRegister.register(resourceKey.location().getPath(), () -> resourceFunction.apply(resourceKey.location()));
            return new DeferredHolder<>(resourceKey);
        }
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
        public Holder<T> register(String name, Function<ResourceLocation, T> resourceFunction) {
            final var deferredRegister = DeferredRegisters.get(registryKey, namespace);
            final var registryObject = deferredRegister.register(name, () -> resourceFunction.apply(ResourceLocation.fromNamespaceAndPath(namespace, name)));
            return new DeferredHolder<>(registryObject.getKey());
        }
    }
}
