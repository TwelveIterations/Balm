package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;

public interface ConfiguredEnum<T extends Enum<T>> extends ConfiguredProperty<T> {
    default T get(LoadedConfig config) {
        return getRaw(config);
    }

    default T get() {
        return get(Balm.getConfig().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, T value) {
        setRaw(config, value);
    }
}
