package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.TableBalmConfig;

import java.util.Set;

public interface ConfiguredSet<T> extends ConfiguredProperty<Set<T>> {
    default Set<T> get(TableBalmConfig config) {
        return getRaw(config);
    }

    default Set<T> get() {
        return get(Balm.getConfig().getActive(parentSchema()));
    }

    default void set(TableBalmConfig config, Set<T> value) {
        setRaw(config, value);
    }
}
