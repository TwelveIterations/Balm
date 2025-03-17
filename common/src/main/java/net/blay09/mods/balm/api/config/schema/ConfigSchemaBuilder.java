package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.config.schema.builder.ConfigCategoryBuilder;
import net.blay09.mods.balm.api.config.schema.builder.PropertyHolderBuilder;

public interface ConfigSchemaBuilder extends PropertyHolderBuilder, BalmConfigSchema {
    ConfigCategoryBuilder category(String name);
}
