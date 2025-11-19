package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;

import java.util.List;

public interface ConfiguredList<T> extends ConfiguredProperty<List<T>>, NestedTypeHolder<T> {
    default List<T> get(LoadedConfig config) {
        return getRaw(config);
    }

    default List<T> get() {
        return get(Balm.config().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, List<T> value) {
        setRaw(config, value);
    }

    default void set(List<T> value) {
        set(Balm.config().getLocalConfig(parentSchema()), value);
    }
}
