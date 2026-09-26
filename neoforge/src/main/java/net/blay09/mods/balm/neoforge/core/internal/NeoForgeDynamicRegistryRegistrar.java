package net.blay09.mods.balm.neoforge.core.internal;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.core.AbstractDynamicRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewDatapackRegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeDynamicRegistryRegistrar {
    private final List<DynamicRegistryData<?>> registries = new ArrayList<>();
    private final List<ReloadableRegistryData<?>> reloadableRegistries = new ArrayList<>();

    public <T> void add(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec, AbstractDynamicRegistryBuilder<T> builder) {
        registries.add(new DynamicRegistryData<>(registryKey, codec, builder));
    }

    public <T> void addReloadable(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec) {
        reloadableRegistries.add(new ReloadableRegistryData<>(registryKey, codec));
    }

    @SubscribeEvent
    public void registerRegistries(NewDatapackRegistryEvent event) {
        for (final var registry : registries) {
            registry.register(event);
        }
        for (final var registry : reloadableRegistries) {
            registry.register(event);
        }
    }

    private record DynamicRegistryData<T>(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec, AbstractDynamicRegistryBuilder<T> builder) {
        @SuppressWarnings("unchecked")
        public void register(NewDatapackRegistryEvent event) {
            final var networkCodec = builder.shouldSync() ? builder.getNetworkCodec() : null;
            event.worldRegistry((ResourceKey<Registry<T>>) registryKey, codec, networkCodec != null ? networkCodec : (builder.shouldSync() ? codec : null));
        }
    }

    private record ReloadableRegistryData<T>(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec) {
        @SuppressWarnings("unchecked")
        public void register(NewDatapackRegistryEvent event) {
            event.reloadableRegistry((ResourceKey<Registry<T>>) registryKey, codec);
        }
    }
}
