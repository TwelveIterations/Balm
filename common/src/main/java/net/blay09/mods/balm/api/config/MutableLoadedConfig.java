package net.blay09.mods.balm.api.config;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

import java.util.function.Predicate;

public interface MutableLoadedConfig extends LoadedConfig {
    <T> void setRaw(ConfiguredProperty<T> property, T value);

    @SuppressWarnings({"rawtypes", "unchecked"})
    default void applyFrom(BalmConfigSchema schema, LoadedConfig config, Predicate<ConfiguredProperty<?>> propertyFilter) {
        for (final var rootProperty : schema.rootProperties()) {
            if (propertyFilter.test(rootProperty)) {
                setRaw((ConfiguredProperty) rootProperty, config.getRaw(rootProperty));
            }
        }
        for (final var category : schema.categories()) {
            for (final var property : category.properties()) {
                if (propertyFilter.test(property)) {
                    setRaw((ConfiguredProperty) property, config.getRaw(property));
                }
            }
        }
    }

    default void applyFrom(BalmConfigSchema schema, LoadedConfig config) {
        applyFrom(schema, config, it -> true);
    }

    MutableLoadedConfig copy();
}
