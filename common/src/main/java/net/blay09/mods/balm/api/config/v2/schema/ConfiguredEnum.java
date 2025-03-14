package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.TableBalmConfig;

public interface ConfiguredEnum<T extends Enum<T>> extends ConfiguredProperty<T> {
    default T get(TableBalmConfig config) {
        return getRaw(config);
    }

    default T get() {
        return get(Balm.getConfig().getActive(parentSchema()));
    }

    default void set(TableBalmConfig config, T value) {
        setRaw(config, value);
    }
}
