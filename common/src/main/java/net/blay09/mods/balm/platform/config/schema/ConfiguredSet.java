package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

import java.util.Set;

public interface ConfiguredSet<T> extends ConfiguredProperty<Set<T>>, NestedTypeHolder<T> {
    default Set<T> get(LoadedConfig config) {
        return getRaw(config);
    }

    default Set<T> get() {
        final var config = Balm.config().getActiveConfig(parentSchema());
        if (config == null) {
            throw new RuntimeException("No active config found for schema " + parentSchema().identifier());
        }
        return get(config);
    }

    default void set(MutableLoadedConfig config, Set<T> value) {
        setRaw(config, value);
    }

    default void set(Set<T> value) {
        final var config = Balm.config().getLocalConfig(parentSchema());
        if (config == null) {
            throw new RuntimeException("No local config loaded for schema " + parentSchema().identifier());
        }
        set(config, value);
    }
}
