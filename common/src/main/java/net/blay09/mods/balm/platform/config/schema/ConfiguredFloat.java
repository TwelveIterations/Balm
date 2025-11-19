package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

public interface ConfiguredFloat extends ConfiguredProperty<Float> {
    default float get(LoadedConfig config) {
        return getRaw(config);
    }

    default float get() {
        return get(Balm.config().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, float value) {
        setRaw(config, value);
    }

    default void set(float value) {
        set(Balm.config().getLocalConfig(parentSchema()), value);
    }
}
