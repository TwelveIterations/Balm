package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;

public interface ConfiguredDouble extends ConfiguredProperty<Double> {
    default double get(LoadedConfig config) {
        return getRaw(config);
    }

    default double get() {
        return get(Balm.getConfig().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, double value) {
        setRaw(config, value);
    }

    default void set(double value) {
        set(Balm.getConfig().getLocalConfig(parentSchema()), value);
    }
}
