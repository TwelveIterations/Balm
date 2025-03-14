package net.blay09.mods.balm.api.config.v2;

import com.google.common.collect.Table;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;

public record TableBalmConfig(Table<String, String, Object> table) implements LoadedBalmConfig, BalmConfigData {
    @Override
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
        if (property.type().isAssignableFrom(value.getClass())) {
            table.put(property.category(), property.name(), value);
        } else {
            throw new IllegalArgumentException("Invalid type for property " + property.name() + " in category " + property.category() + ": " + value.getClass()
                    .getName() + ", expected " + property.type().getName());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getRaw(ConfiguredProperty<T> property) {
        final var value = table.get(property.category(), property.name());
        if (value == null) {
            return property.defaultValue();
        }
        if (!property.type().isAssignableFrom(value.getClass())) {
            return property.defaultValue();
        }
        return (T) value;
    }
}
