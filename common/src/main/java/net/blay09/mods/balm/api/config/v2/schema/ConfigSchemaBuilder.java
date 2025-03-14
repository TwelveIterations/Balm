package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.config.v2.schema.builder.ConfigCategoryBuilder;
import net.blay09.mods.balm.api.config.v2.schema.builder.ConfigPropertyBuilder;

public interface ConfigSchemaBuilder {
    ConfigPropertyBuilder property(String name);
    ConfigCategoryBuilder category(String name);
}
