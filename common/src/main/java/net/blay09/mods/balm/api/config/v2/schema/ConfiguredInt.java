package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.TableBalmConfig;

public interface ConfiguredInt extends ConfiguredProperty<Integer> {
    default int get(TableBalmConfig config) {
        return getRaw(config);
    }

    default int get() {
        return get(Balm.getConfig().getActive(parentSchema()));
    }

    default void set(TableBalmConfig config, int value) {
        setRaw(config, value);
    }
}
