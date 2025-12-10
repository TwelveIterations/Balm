package net.blay09.mods.balm.forge.config;

import com.google.common.collect.Table;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.blay09.mods.balm.platform.config.PropertyAwareConfig;
import net.blay09.mods.balm.platform.config.internal.LoadedTableConfig;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public record LoadedForgeConfig(BalmConfigSchema schema, ModConfig modConfig,
                                Table<String, String, ForgeConfigSpec.ConfigValue<?>> properties) implements MutableLoadedConfig, PropertyAwareConfig {

    @Override
    @SuppressWarnings("unchecked")
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
        @SuppressWarnings("unchecked") final var backingProperty = (ForgeConfigSpec.ConfigValue<T>) properties.get(property.category(), property.name());
        if (backingProperty != null) {
            final var mappedValue = ForgeBalmConfig.mapConfigValueToNeoForge(value);
            backingProperty.set((T) mappedValue);
        }
    }

    @Override
    public MutableLoadedConfig copy() {
        final var newConfig = new LoadedTableConfig();
        newConfig.applyFrom(schema, this);
        return newConfig;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getRaw(ConfiguredProperty<T> property) {
        final var backingProperty = properties.get(property.category(), property.name());
        if (backingProperty != null) {
            final var value = backingProperty.get();
            return (T) ForgeBalmConfig.mapConfigValueFromNeoForge(property, value);
        }
        return property.defaultValue();
    }

    @Override
    public MutableLoadedConfig mutable(BalmConfigSchema schema) {
        return this;
    }

    @Override
    public boolean hasProperty(ConfiguredProperty<?> property) {
        return properties.contains(property.category(), property.name());
    }
}
