package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.TableBalmConfig;

public interface ConfiguredBoolean extends ConfiguredProperty<Boolean> {
    default boolean get(TableBalmConfig config) {
        return getRaw(config);
    }

    default boolean get() {
        return get(Balm.getConfig().getActive(parentSchema()));
    }

    default void set(TableBalmConfig config, boolean value) {
        setRaw(config, value);
    }
}
