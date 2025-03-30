package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;

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

    default void set(String value) {
        set(Balm.getConfig().getLocalConfig(parentSchema()), value);
    }
}
