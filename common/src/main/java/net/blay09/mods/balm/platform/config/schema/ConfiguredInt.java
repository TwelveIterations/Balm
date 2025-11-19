package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

public interface ConfiguredInt extends ConfiguredProperty<Integer> {
    default int get(LoadedConfig config) {
        return getRaw(config);
    }

    default int get() {
        return get(Balm.config().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, int value) {
        setRaw(config, value);
    }

    default void set(int value) {
        set(Balm.config().getLocalConfig(parentSchema()), value);
    }
}
