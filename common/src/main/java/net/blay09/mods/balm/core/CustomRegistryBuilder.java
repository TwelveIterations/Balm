package net.blay09.mods.balm.core;

import net.minecraft.resources.ResourceLocation;

/**
 * Builder options for custom code-driven registries.
 */
public interface CustomRegistryBuilder<T> {
    /**
     * Sets the default entry key for defaulted registries.
     */
    CustomRegistryBuilder<T> defaultKey(ResourceLocation defaultKey);

    /**
     * Enables registry synchronization where supported.
     */
    CustomRegistryBuilder<T> sync();
}
