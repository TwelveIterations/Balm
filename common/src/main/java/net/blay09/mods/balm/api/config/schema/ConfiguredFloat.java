package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;

public interface ConfiguredFloat extends ConfiguredProperty<Float> {
    default float get(LoadedConfig config) {
        return getRaw(config);
    }

    default float get() {
        return get(Balm.getConfig().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, float value) {
        setRaw(config, value);
    }

    default void set(float value) {
        set(Balm.getConfig().getLocalConfig(parentSchema()), value);
    }
}
