package net.blay09.mods.balm.api.config.reflection;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedTableConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

public record LoadedReflectionConfig<ConfigData>(ConfigData data) implements MutableLoadedConfig {

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
        newConfig.applyFrom(Balm.getConfig().getSchema(data.getClass()), this);
        return newConfig;
    }

    @Override
    public MutableLoadedConfig mutable(BalmConfigSchema schema) {
        return this;
    }

    private Object locatePropertyHolder(ConfiguredProperty<?> property) throws NoSuchFieldException, IllegalAccessException {
        final var category = property.category();
        if (category != null && !category.isEmpty()) {
            final var categoryField = data.getClass().getField(category);
            return categoryField.get(data);
        } else {
            return data;
        }
    }
}