package net.blay09.mods.balm.api.config.v2.schema;

import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.schema.impl.ConfigSchemaImpl;
import net.minecraft.resources.ResourceLocation;

public interface BalmConfigSchema {
    static ConfigSchemaImpl create(ResourceLocation identifier) {
        return new ConfigSchemaImpl(identifier);
    }

    ResourceLocation identifier();

    LoadedConfig defaults();
}
