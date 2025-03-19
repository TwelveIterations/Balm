package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
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

    default void set(ResourceLocation value) {
        set(Balm.getConfig().getLocalConfig(parentSchema()), value);
    }
}
