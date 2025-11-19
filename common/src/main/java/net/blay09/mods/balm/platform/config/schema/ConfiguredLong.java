package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

public interface ConfiguredLong extends ConfiguredProperty<Long> {
    default long get(LoadedConfig config) {
        return getRaw(config);
    }

    default long get() {
        return get(Balm.config().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, long value) {
        setRaw(config, value);
    }

    default void set(long value) {
        set(Balm.config().getLocalConfig(parentSchema()), value);
    }
}
