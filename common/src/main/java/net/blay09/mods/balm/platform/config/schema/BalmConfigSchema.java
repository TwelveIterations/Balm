package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.platform.config.schema.internal.ConfigSchemaImpl;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public interface BalmConfigSchema {
    static ConfigSchemaImpl create(Identifier identifier) {
        return new ConfigSchemaImpl(identifier);
    }

    Identifier identifier();

    LoadedConfig defaults();

    Collection<ConfiguredProperty<?>> rootProperties();

    Collection<ConfigCategory> categories();

    @Nullable
    ConfigCategory findCategory(String category);

    @Nullable
    ConfiguredProperty<?> findProperty(String category, String property);

    @Nullable
    ConfiguredProperty<?> findRootProperty(String property);
}
