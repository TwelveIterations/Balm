package net.blay09.mods.balm.platform.config.reflection.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.blay09.mods.balm.platform.config.PropertyAwareConfig;
import net.blay09.mods.balm.platform.config.internal.LoadedTableConfig;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;

public record LoadedReflectionConfig<ConfigData>(ConfigData data) implements MutableLoadedConfig, PropertyAwareConfig {

    @Override
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
        try {
            final var holder = locatePropertyHolder(property);
            final var field = holder.getClass().getField(property.name());
            field.set(holder, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set config property '" +
                    (property.category().isEmpty() ? "" : property.category() + ".") +
                    property.name() + "'", e);
        }
    }

    @Override
    public <T> T getRaw(ConfiguredProperty<T> property) {
        try {
            final var holder = locatePropertyHolder(property);
            final var field = holder.getClass().getField(property.name());
            @SuppressWarnings("unchecked")
            T value = (T) field.get(holder);
            return value;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get config property '" +
                    (property.category().isEmpty() ? "" : property.category() + ".") +
                    property.name() + "'", e);
        }
    }

    @Override
    public MutableLoadedConfig copy() {
        final var newConfig = new LoadedTableConfig();
        final var schema = Balm.config().getSchema(data.getClass());
        if (schema == null) {
            throw new RuntimeException("No config schema found for " + data.getClass().getName());
        }
        newConfig.applyFrom(schema, this);
        return newConfig;
    }

    @Override
    public MutableLoadedConfig mutable(BalmConfigSchema schema) {
        return this;
    }

    private Object locatePropertyHolder(ConfiguredProperty<?> property) throws NoSuchFieldException, IllegalAccessException {
        final var category = property.category();
        if (!category.isEmpty()) {
            final var categoryField = data.getClass().getField(category);
            return categoryField.get(data);
        } else {
            return data;
        }
    }

    @Override
    public boolean hasProperty(ConfiguredProperty<?> property) {
        try {
            final var holder = locatePropertyHolder(property);
            holder.getClass().getField(property.name());
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}