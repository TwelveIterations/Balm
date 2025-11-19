package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

public interface ConfiguredBoolean extends ConfiguredProperty<Boolean> {
    default boolean get(LoadedConfig config) {
        return getRaw(config);
    }

    default boolean get() {
        final var config = Balm.config().getActiveConfig(parentSchema());
        if (config == null) {
            throw new RuntimeException("No active config found for schema " + parentSchema().identifier());
        }
        return get(config);
    }

    default void set(MutableLoadedConfig config, boolean value) {
        setRaw(config, value);
    }

    default void set(boolean value) {
        final var config = Balm.config().getLocalConfig(parentSchema());
        if (config == null) {
            throw new RuntimeException("No local config loaded for schema " + parentSchema().identifier());
        }
        set(config, value);
    }
}
