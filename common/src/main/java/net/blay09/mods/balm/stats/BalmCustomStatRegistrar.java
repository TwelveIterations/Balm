package net.blay09.mods.balm.stats;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;

public interface BalmCustomStatRegistrar {
    /**
     * Registers a custom stat with default formatting.
     * You **must** use the returned {@link ResourceLocation} instance when awarding the stat or otherwise interacting with it, as the custom stat registry checks identity rather than equality.
     *
     * @return the resource location of the registered stat.
     */
    default ResourceLocation register(String name) {
        return register(name, StatFormatter.DEFAULT);
    }

    /**
     * Registers a custom stat with the given formatter.
     * You **must** use the returned {@link ResourceLocation} instance when awarding the stat or otherwise interacting with it, as the custom stat registry checks identity rather than equality.
     *
     * @return the resource location of the registered stat.
     */
    ResourceLocation register(String name, StatFormatter formatter);

    /**
     * Registers a custom stat with the given formatter.
     *
     * @return the resource location that was passed in.
     */
    ResourceLocation register(ResourceLocation statIdentifier, StatFormatter formatter);

}
