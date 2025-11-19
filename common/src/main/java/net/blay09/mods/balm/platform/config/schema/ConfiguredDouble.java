package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

public interface ConfiguredDouble extends ConfiguredProperty<Double> {
    default double get(LoadedConfig config) {
        return getRaw(config);
    }

    default double get() {
        return get(Balm.config().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, double value) {
        setRaw(config, value);
    }

    default void set(double value) {
        set(Balm.config().getLocalConfig(parentSchema()), value);
    }
}
