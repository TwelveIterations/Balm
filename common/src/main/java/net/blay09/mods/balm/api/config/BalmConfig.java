package net.blay09.mods.balm.api.config;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.network.ConfigReflection;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.util.Collection;

public interface BalmConfig {
    void registerConfig(BalmConfigSchema schema);

    BalmConfigSchema getSchema(ResourceLocation identifier);

    MutableLoadedConfig getLocalConfig(ResourceLocation identifier);

    LoadedConfig getActiveConfig(ResourceLocation identifier);

    File getConfigDir();

    default MutableLoadedConfig getLocalConfig(BalmConfigSchema schema) {
        return getLocalConfig(schema.identifier());
    }

    default LoadedConfig getActiveConfig(BalmConfigSchema schema) {
        return getActiveConfig(schema.identifier());
    }

    default BalmConfigSchema registerConfig(Class<?> configDataClass) {
        final var schema = ConfigReflection.schemaOf(configDataClass);
        registerConfig(schema);
        return schema;
    }

    default BalmConfigSchema getSchema(Class<?> configDataClass) {
        return getSchema(ConfigReflection.getIdentifier(configDataClass));
    }

    default <T> T getActiveConfig(Class<T> configDataClass) {
        final var loadedConfig = getActiveConfig(getSchema(configDataClass));
        return ConfigReflection.of(configDataClass, loadedConfig); // TODO would be nice to cache this
    }

    default <T> T getLocalConfig(Class<T> configDataClass) {
        final var loadedConfig = getLocalConfig(getSchema(configDataClass));
        return ConfigReflection.of(configDataClass, loadedConfig); // TODO would be nice to cache this
    }

    Collection<BalmConfigSchema> getSchemasByNamespace(String namespace);

    Collection<BalmConfigSchema> getSchemas();

    void saveLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config);
}
