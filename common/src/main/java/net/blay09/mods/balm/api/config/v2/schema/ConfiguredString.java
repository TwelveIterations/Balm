package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;

public interface ConfiguredString extends ConfiguredProperty<String> {
    default String get(LoadedConfig config) {
        return getRaw(config);
    }

    default String get() {
        return get(Balm.getConfig().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, String value) {
        setRaw(config, value);
    }
}
