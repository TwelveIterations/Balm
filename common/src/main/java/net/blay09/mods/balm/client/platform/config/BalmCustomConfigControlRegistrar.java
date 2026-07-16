package net.blay09.mods.balm.client.platform.config;

import net.minecraft.resources.Identifier;

public interface BalmCustomConfigControlRegistrar {
    <T> void register(String path, ConfigControl<T> control);

    <T> void register(Identifier identifier, ConfigControl<T> control);
}
