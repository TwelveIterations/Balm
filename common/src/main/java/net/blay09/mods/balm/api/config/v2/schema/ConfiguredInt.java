package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;

public interface ConfiguredInt extends ConfiguredProperty<Integer> {
    default int get(LoadedConfig config) {
        return getRaw(config);
    }

    default int get() {
        return get(Balm.getConfig().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, int value) {
        setRaw(config, value);
    }
}
