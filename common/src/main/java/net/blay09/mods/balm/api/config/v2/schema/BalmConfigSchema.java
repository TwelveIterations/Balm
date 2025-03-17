package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.schema.builder.ConfigCategory;
import net.blay09.mods.balm.api.config.v2.schema.impl.ConfigSchemaImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

public interface BalmConfigSchema {
    static ConfigSchemaImpl create(ResourceLocation identifier) {
        return new ConfigSchemaImpl(identifier);
    }

    ResourceLocation identifier();

    LoadedConfig defaults();

    Collection<ConfiguredProperty<?>> rootProperties();

    Collection<ConfigCategory> categories();

    ConfiguredProperty<?> findProperty(String category, String property);

    ConfiguredProperty<?> findRootProperty(String property);
}
