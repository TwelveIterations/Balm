package net.blay09.mods.balm.core;

import com.mojang.serialization.Codec;

/**
 * Builder options for custom dynamic registries.
 */
public interface DynamicRegistryBuilder<T> {
    /**
     * Enables registry synchronization using the registry's datapack codec.
     */
    DynamicRegistryBuilder<T> sync();

    /**
     * Enables registry synchronization using a separate network codec.
     */
    DynamicRegistryBuilder<T> sync(Codec<T> networkCodec);

    /**
     * Skips synchronization when the registry has no entries, where supported.
     */
    DynamicRegistryBuilder<T> skipSyncWhenEmpty();
}
