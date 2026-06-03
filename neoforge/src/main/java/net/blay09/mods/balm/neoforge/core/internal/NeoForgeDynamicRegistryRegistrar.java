package net.blay09.mods.balm.neoforge.core.internal;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.core.AbstractDynamicRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import java.util.ArrayList;
import java.util.List;

public class NeoForgeDynamicRegistryRegistrar {
    private final List<DynamicRegistryData<?>> registries = new ArrayList<>();

    public <T> void add(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec, AbstractDynamicRegistryBuilder<T> builder) {
        registries.add(new DynamicRegistryData<>(registryKey, codec, builder));
    }

    @SubscribeEvent
    public void registerRegistries(DataPackRegistryEvent.NewRegistry event) {
        for (final var registry : registries) {
            registry.register(event);
        }
    }

    private record DynamicRegistryData<T>(ResourceKey<? extends Registry<T>> registryKey, Codec<T> codec, AbstractDynamicRegistryBuilder<T> builder) {
        @SuppressWarnings("unchecked")
        public void register(DataPackRegistryEvent.NewRegistry event) {
            final var networkCodec = builder.shouldSync() ? builder.getNetworkCodec() : null;
            event.dataPackRegistry((ResourceKey<Registry<T>>) registryKey, codec, networkCodec != null ? networkCodec : (builder.shouldSync() ? codec : null));
        }
    }
}
