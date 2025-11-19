package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;

import java.util.Set;

public interface ConfiguredSet<T> extends ConfiguredProperty<Set<T>>, NestedTypeHolder<T> {
    default Set<T> get(LoadedConfig config) {
        return getRaw(config);
    }

    default Set<T> get() {
        return get(Balm.config().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, Set<T> value) {
        setRaw(config, value);
    }

    default void set(Set<T> value) {
        set(Balm.config().getLocalConfig(parentSchema()), value);
    }
}
