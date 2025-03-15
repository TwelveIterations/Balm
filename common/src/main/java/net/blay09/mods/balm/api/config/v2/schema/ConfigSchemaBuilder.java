package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.config.v2.schema.builder.ConfigCategoryBuilder;
import net.blay09.mods.balm.api.config.v2.schema.builder.PropertyHolderBuilder;

public interface ConfigSchemaBuilder extends PropertyHolderBuilder {
    ConfigCategoryBuilder category(String name);
}
