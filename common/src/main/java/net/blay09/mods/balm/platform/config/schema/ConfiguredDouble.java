package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;

import java.util.Optional;

public interface ConfiguredDouble extends ConfiguredProperty<Double> {
    default Optional<Double> minValue() {
        return Optional.empty();
    }

    default Optional<Double> maxValue() {
        return Optional.empty();
    }

    default double get(LoadedConfig config) {
        return getRaw(config);
    }

    default double get() {
        final var config = Balm.config().getActiveConfig(parentSchema());
        if (config == null) {
            throw new RuntimeException("No active config found for schema " + parentSchema().identifier());
        }
        return get(config);
    }

    default void set(MutableLoadedConfig config, double value) {
        setRaw(config, value);
    }

    default void set(double value) {
        final var config = Balm.config().getLocalConfig(parentSchema());
        if (config == null) {
            throw new RuntimeException("No local config loaded for schema " + parentSchema().identifier());
        }
        set(config, value);
    }
}
