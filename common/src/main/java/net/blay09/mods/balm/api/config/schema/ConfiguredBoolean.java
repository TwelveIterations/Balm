package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;

public interface ConfiguredBoolean extends ConfiguredProperty<Boolean> {
    default boolean get(LoadedConfig config) {
        return getRaw(config);
    }

    default boolean get() {
        return get(Balm.config().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, boolean value) {
        setRaw(config, value);
    }

    default void set(boolean value) {
        set(Balm.config().getLocalConfig(parentSchema()), value);
    }
}
