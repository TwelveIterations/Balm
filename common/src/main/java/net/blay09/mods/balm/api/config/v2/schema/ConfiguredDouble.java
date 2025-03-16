package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;

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
}
