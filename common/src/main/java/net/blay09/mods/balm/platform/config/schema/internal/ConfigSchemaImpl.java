package net.blay09.mods.balm.platform.config.schema.internal;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.blay09.mods.balm.platform.config.internal.DefaultedConfig;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfigSchemaBuilder;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.platform.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.platform.config.schema.builder.ConfigCategoryBuilder;
import net.blay09.mods.balm.platform.config.schema.builder.ConfigPropertyBuilder;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConfigSchemaImpl implements BalmConfigSchema, ConfigSchemaBuilder {

    private final Identifier identifier;
    private final Map<String, ConfigCategory> categories = new HashMap<>();
    private final Map<String, ConfiguredProperty<?>> rootProperties = new HashMap<>();
    private final Table<String, String, ConfiguredProperty<?>> properties = HashBasedTable.create();

    public ConfigSchemaImpl(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public Identifier identifier() {
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
        return rootProperties.values();
    }

    @Override
    public Collection<ConfigCategory> categories() {
        return categories.values();
    }

    @Override
    @Nullable
    public ConfiguredProperty<?> findProperty(String category, String property) {
        return properties.get(category, property);
    }

    @Override
    @Nullable
    public ConfiguredProperty<?> findRootProperty(String property) {
        return properties.get("", property);
    }

    public <T extends ConfiguredProperty<?>> T addAndReturn(T property) {
        properties.put(property.category(), property.name(), property);
        if (property.category().isEmpty()) {
            rootProperties.put(property.name(), property);
        } else {
            if (categories.get(property.category()) instanceof ConfigCategoryImpl category) {
                category.addProperty(property);
            }
        }
        return property;
    }
}
