package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.TableBalmConfig;

public interface ConfiguredFloat extends ConfiguredProperty<Float> {
    default float get(TableBalmConfig config) {
        return getRaw(config);
    }

    default float get() {
        return get(Balm.getConfig().getActive(parentSchema()));
    }

    default void set(TableBalmConfig config, float value) {
        setRaw(config, value);
    }
}
