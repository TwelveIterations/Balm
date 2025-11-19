package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

public interface ConfiguredEnum<T extends Enum<T>> extends ConfiguredProperty<T> {
    default T get(LoadedConfig config) {
        return getRaw(config);
    }

    default T get() {
        return get(Balm.config().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, T value) {
        setRaw(config, value);
    }

    default void set(T value) {
        set(Balm.config().getLocalConfig(parentSchema()), value);
    }
}
