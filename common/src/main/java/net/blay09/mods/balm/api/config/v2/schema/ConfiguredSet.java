package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.TableBalmConfig;

import java.util.Set;

public interface ConfiguredSet<T> extends ConfiguredProperty<Set<T>>, NestedTypeHolder<T> {
    default Set<T> get(LoadedConfig config) {
        return getRaw(config);
    }

    default Set<T> get() {
        return get(Balm.getConfig().getActiveConfig(parentSchema()));
    }

    default void set(TableBalmConfig config, Set<T> value) {
        setRaw(config, value);
    }
}
