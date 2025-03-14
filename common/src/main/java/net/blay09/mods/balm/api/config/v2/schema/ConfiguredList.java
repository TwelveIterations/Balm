package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.TableBalmConfig;

import java.util.List;

public interface ConfiguredList<T> extends ConfiguredProperty<List<T>> {
    default List<T> get(TableBalmConfig config) {
        return getRaw(config);
    }

    default List<T> get() {
        return get(Balm.getConfig().getActive(parentSchema()));
    }

    default void set(TableBalmConfig config, List<T> value) {
        setRaw(config, value);
    }
}
