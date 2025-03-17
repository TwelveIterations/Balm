package net.blay09.mods.balm.api.config.v2;

import net.blay09.mods.balm.api.config.v2.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;

public interface MutableLoadedConfig extends LoadedConfig {
    <T> void setRaw(ConfiguredProperty<T> property, T value);

    @SuppressWarnings({"rawtypes", "unchecked"})
    default void applyFrom(BalmConfigSchema schema, LoadedConfig config) {
        for (final var rootProperty : schema.rootProperties()) {
            setRaw((ConfiguredProperty) rootProperty, config.getRaw(rootProperty));
        }
        for (final var category : schema.categories()) {
            for (final var property : category.properties()) {
                setRaw((ConfiguredProperty) property, config.getRaw(property));
            }
        }
    }
}
