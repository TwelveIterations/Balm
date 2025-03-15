package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;

import java.util.List;

public interface ConfiguredList<T> extends ConfiguredProperty<List<T>>, NestedTypeHolder<T> {
    default List<T> get(LoadedConfig config) {
        return getRaw(config);
    }

    default List<T> get() {
        return get(Balm.getConfig().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, List<T> value) {
        setRaw(config, value);
    }
}
