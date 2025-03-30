package net.blay09.mods.balm.neoforge.config;

import com.google.common.collect.Table;
import net.blay09.mods.balm.api.config.LoadedTableConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public record LoadedNeoForgeConfig(BalmConfigSchema schema, ModConfig modConfig,
                                   Table<String, String, ModConfigSpec.ConfigValue<?>> properties) implements MutableLoadedConfig {

    @Override
    @SuppressWarnings("unchecked")
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
        @SuppressWarnings("unchecked") final var backingProperty = (ModConfigSpec.ConfigValue<T>) properties.get(property.category(), property.name());
        if (backingProperty != null) {
            final var mappedValue = NeoForgeBalmConfig.mapConfigValueToNeoForge(value);
            backingProperty.set((T) mappedValue);
        }
    }

    @Override
    public MutableLoadedConfig copy() {
        final var newConfig = new LoadedTableConfig();
        newConfig.applyFrom(schema, newConfig);
        return newConfig;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getRaw(ConfiguredProperty<T> property) {
        final var backingProperty = properties.get(property.category(), property.name());
        if (backingProperty != null) {
            final var value = backingProperty.get();
            return (T) NeoForgeBalmConfig.mapConfigValueFromNeoForge(property, value);
        }
        return property.defaultValue();
    }

    @Override
    public MutableLoadedConfig mutable(BalmConfigSchema schema) {
        return this;
    }

}
