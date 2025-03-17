package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;

public interface ConfiguredLong extends ConfiguredProperty<Long> {
    default long get(LoadedConfig config) {
        return getRaw(config);
    }

    default long get() {
        return get(Balm.getConfig().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, long value) {
        setRaw(config, value);
    }
}
