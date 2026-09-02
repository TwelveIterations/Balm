package net.blay09.mods.balm.core;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmRegistrar {
    /**
     * Creates a custom code-driven registry.
     *
     * @param registryKey the registry key to create.
     * @return the created registry.
     */
    default <T> Registry<T> createCustomRegistry(ResourceKey<? extends Registry<T>> registryKey) {
        return createCustomRegistry(registryKey, _ -> {
        });
    }

    /**
     * Creates a custom code-driven defaulted registry.
     *
     * @param registryKey the registry key to create.
     * @return the created registry.
     */
    default <T> Registry<T> createCustomRegistry(ResourceKey<? extends Registry<T>> registryKey, Identifier defaultKey) {
        return createCustomRegistry(registryKey, builder -> {
            builder.defaultKey(defaultKey);
        });
    }

    /**
     * Creates a custom code-driven registry with additional options. Call this during mod initialization, before registering
     * entries to the registry.
     *
     * @param registryKey      the registry key to create.
     * @param builderConsumer callback for configuring the registry.
     * @return the created registry.
     */
    <T> Registry<T> createCustomRegistry(ResourceKey<? extends Registry<T>> registryKey, Consumer<CustomRegistryBuilder<T>> builderConsumer);

    /**
     * Creates a custom dynamic registry loaded from datapacks. Entries are available from {@link net.minecraft.core.RegistryAccess}
     * once a world has loaded.
     *
     * @param registryKey the registry key to create.
     * @param codec       the codec used to load registry entries from datapacks.
     */
    default <T> void createDynamicRegistry(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec) {
        createDynamicRegistry(registryKey, codec, _ -> {
        });
    }

    /**
     * Creates a custom dynamic registry loaded from datapacks with additional options. Call this during mod initialization.
     *
     * @param registryKey      the registry key to create.
     * @param codec            the codec used to load registry entries from datapacks.
     * @param builderConsumer callback for configuring the registry.
     */
    <T> void createDynamicRegistry(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec, Consumer<DynamicRegistryBuilder<T>> builderConsumer);

    default <T> BalmHolderRegistration<T> register(ResourceKey<T> resourceKey, Supplier<T> resourceSupplier) {
        return register(resourceKey, (id) -> resourceSupplier.get());
    }

    <T> BalmHolderRegistration<T> register(ResourceKey<T> resourceKey, Function<Identifier, T> resourceFunction);

    <T> void addAlias(ResourceKey<? extends Registry<T>> registryKey, Identifier oldId, Identifier newId);

    <T> Scoped<T> scoped(ResourceKey<? extends Registry<T>> registryKey, String namespace);

    interface Scoped<T> {
        BalmHolderRegistration<T> register(String name, Function<Identifier, T> resourceFunction);

        void addAlias(String oldName, String newName);

        void addAlias(Identifier oldId, Identifier newId);
    }
}
