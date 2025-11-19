package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

public interface ConfiguredString extends ConfiguredProperty<String> {
    default String get(LoadedConfig config) {
        return getRaw(config);
    }

    default String get() {
        return get(Balm.config().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, String value) {
        setRaw(config, value);
    }

    default void set(String value) {
        set(Balm.config().getLocalConfig(parentSchema()), value);
    }
}
