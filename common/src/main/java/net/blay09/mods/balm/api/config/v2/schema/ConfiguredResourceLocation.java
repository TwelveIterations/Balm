package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;
import net.minecraft.resources.ResourceLocation;

public interface ConfiguredResourceLocation extends ConfiguredProperty<ResourceLocation> {
    default ResourceLocation get(LoadedConfig config) {
        return getRaw(config);
    }

    default ResourceLocation get() {
        return get(Balm.getConfig().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, ResourceLocation value) {
        setRaw(config, value);
    }
}
