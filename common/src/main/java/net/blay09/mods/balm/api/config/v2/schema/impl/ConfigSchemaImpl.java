package net.blay09.mods.balm.api.config.v2.schema.impl;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.blay09.mods.balm.api.config.v2.DefaultedConfig;
import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.v2.schema.ConfigSchemaBuilder;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.v2.schema.builder.ConfigCategory;
import net.blay09.mods.balm.api.config.v2.schema.builder.ConfigCategoryBuilder;
import net.blay09.mods.balm.api.config.v2.schema.builder.ConfigPropertyBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConfigSchemaImpl implements BalmConfigSchema, ConfigSchemaBuilder {

    private final ResourceLocation identifier;
    private final Map<String, ConfigCategory> categories = new HashMap<>();
    private final Table<String, String, ConfiguredProperty<?>> properties = HashBasedTable.create();

    public ConfigSchemaImpl(ResourceLocation identifier) {
        this.identifier = identifier;
    }

    @Override
    public ResourceLocation identifier() {
        return identifier;
    }

    @Override
    public ConfigPropertyBuilder property(String name) {
        return new ConfigPropertyBuilder(this, name);
    }

    @Override
    public ConfigCategoryBuilder category(String name) {
        final var category = new ConfigCategoryImpl(this, name);
        categories.put(name, category);
        return category;
    }

    @Override
    public LoadedConfig defaults() {
        return DefaultedConfig.INSTANCE;
    }

    @Override
    public Collection<ConfiguredProperty<?>> rootProperties() {
        return properties.values();
    }

    @Override
    public Collection<ConfigCategory> categories() {
        return categories.values();
    }

    @Override
    public ConfiguredProperty<?> findProperty(String category, String property) {
        return properties.get(category, property);
    }

    @Override
    public ConfiguredProperty<?> findRootProperty(String property) {
        return properties.get("", property);
    }

    public <T extends ConfiguredProperty<?>> T addAndReturn(T property) {
        properties.put(property.category(), property.name(), property);
        if (categories.get(property.category()) instanceof ConfigCategoryImpl category) {
            category.addProperty(property);
        }
        return property;
    }
}
