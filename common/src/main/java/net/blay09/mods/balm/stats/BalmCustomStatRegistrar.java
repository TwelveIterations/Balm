package net.blay09.mods.balm.stats;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;

public interface BalmCustomStatRegistrar {
    /**
     * Registers a custom stat with default formatting.
     */
    default ResourceLocation register(String name) {
        return register(name, StatFormatter.DEFAULT);
    }

    /**
     * Registers a custom stat with the given formatter.
     */
    ResourceLocation register(String name, StatFormatter formatter);
}
