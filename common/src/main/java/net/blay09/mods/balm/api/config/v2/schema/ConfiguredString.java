package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.TableBalmConfig;

public interface ConfiguredString extends ConfiguredProperty<String> {
    default String get(TableBalmConfig config) {
        return getRaw(config);
    }

    default String get() {
        return get(Balm.getConfig().getActive(parentSchema()));
    }

    default void set(TableBalmConfig config, String value) {
        setRaw(config, value);
    }
}
