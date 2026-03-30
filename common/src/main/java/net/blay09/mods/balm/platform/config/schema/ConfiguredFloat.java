package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

import java.util.Optional;

public interface ConfiguredFloat extends ConfiguredProperty<Float> {
    default Optional<Float> minValue() {
        return Optional.empty();
    }

    default Optional<Float> maxValue() {
        return Optional.empty();
    }

    default float get(LoadedConfig config) {
        return getRaw(config);
    }

    default float get() {
        final var config = Balm.config().getActiveConfig(parentSchema());
        if (config == null) {
            throw new RuntimeException("No active config found for schema " + parentSchema().identifier());
        }
        return get(config);
    }

    default void set(MutableLoadedConfig config, float value) {
        setRaw(config, value);
    }

    default void set(float value) {
        final var config = Balm.config().getLocalConfig(parentSchema());
        if (config == null) {
            throw new RuntimeException("No local config loaded for schema " + parentSchema().identifier());
        }
        set(config, value);
    }
}
