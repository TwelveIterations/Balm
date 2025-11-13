package net.blay09.mods.balm.api.config;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.network.ConfigReflection;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.util.Collection;
import java.util.function.Consumer;

public interface BalmConfig {
    void registerConfig(BalmConfigSchema schema);

    BalmConfigSchema getSchema(ResourceLocation identifier);

    MutableLoadedConfig getLocalConfig(ResourceLocation identifier);

    LoadedConfig getActiveConfig(ResourceLocation identifier);

    File getConfigDir();

    default File getConfigDir(BalmConfigSchema schema) {
        return getConfigDir();
    }

    default File getConfigFile(BalmConfigSchema schema) {
        final var identifier = schema.identifier();
        return new File(getConfigDir(schema), identifier.getNamespace() + "-" + identifier.getPath() + ".toml");
    }

    default MutableLoadedConfig getLocalConfig(BalmConfigSchema schema) {
        return getLocalConfig(schema.identifier());
    }

    <T> void updateLocalConfig(Class<T> configDataClass, Consumer<T> updater);

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
        return ConfigReflection.of(configDataClass, loadedConfig).data();
    }

    Collection<BalmConfigSchema> getSchemasByNamespace(String namespace);

    Collection<BalmConfigSchema> getSchemas();

    default void saveLocalConfig(BalmConfigSchema schema) {
        saveLocalConfig(schema, getLocalConfig(schema));
    }

    void saveLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config);

    void onConfigAvailable(BalmConfigSchema schema, Consumer<MutableLoadedConfig> handler);

    default <T> void onConfigAvailable(Class<T> configDataClass, Consumer<T> handler) {
        onConfigAvailable(getSchema(configDataClass), (config) -> handler.accept(getActiveConfig(configDataClass)));
    }
}
