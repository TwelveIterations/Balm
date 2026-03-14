package net.blay09.mods.balm.platform.config;

import net.blay09.mods.balm.client.platform.config.BalmConfigScreenFactory;
import net.blay09.mods.balm.platform.config.reflection.internal.ConfigReflection;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public interface BalmConfig {
    void registerConfig(BalmConfigSchema schema);

    @Nullable
    BalmConfigSchema getSchema(Identifier identifier);

    @Nullable
    MutableLoadedConfig getLocalConfig(Identifier identifier);

    @Nullable
    LoadedConfig getActiveConfig(Identifier identifier);

    File getConfigDir();

    default File getConfigDir(BalmConfigSchema schema) {
        return getConfigDir();
    }

    default File getConfigFile(BalmConfigSchema schema) {
        final var identifier = schema.identifier();
        return new File(getConfigDir(schema), identifier.getNamespace() + "-" + identifier.getPath() + ".toml");
    }

    @Nullable
    default MutableLoadedConfig getLocalConfig(BalmConfigSchema schema) {
        return getLocalConfig(schema.identifier());
    }

    <T> void updateLocalConfig(Class<T> configDataClass, Consumer<T> updater);

    @Nullable
    default LoadedConfig getActiveConfig(BalmConfigSchema schema) {
        return getActiveConfig(schema.identifier());
    }

    default BalmConfigSchema registerConfig(Class<?> configDataClass) {
        final var schema = ConfigReflection.schemaOf(configDataClass);
        registerConfig(schema);
        return schema;
    }

    @Nullable
    default BalmConfigSchema getSchema(Class<?> configDataClass) {
        return getSchema(ConfigReflection.getIdentifier(configDataClass));
    }

    @Nullable
    default <T> T getActiveConfig(Class<T> configDataClass) {
        final var schema = getSchema(configDataClass);
        if (schema != null) {
            final var loadedConfig = getActiveConfig(schema);
            if (loadedConfig != null) {
                return ConfigReflection.of(configDataClass, loadedConfig).data();
            }
        }
        return null;
    }

    Collection<BalmConfigSchema> getSchemasByNamespace(String namespace);

    Collection<BalmConfigSchema> getSchemas();

    void setPreferredConfigScreen(String modId, String providerId);

    void setPreferredConfigScreen(String modId, List<String> providerIds);

    @Nullable
    BalmConfigScreenFactory getConfigScreenFactory(String modId);

    default void saveLocalConfig(BalmConfigSchema schema) {
        final var config = getLocalConfig(schema);
        if (config != null) {
            saveLocalConfig(schema, config);
        }
    }

    void saveLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config);

    void onConfigAvailable(BalmConfigSchema schema, Consumer<MutableLoadedConfig> handler);

    default <T> void onConfigAvailable(Class<T> configDataClass, Consumer<T> handler) {
        final var schema = getSchema(configDataClass);
        if (schema != null) {
            onConfigAvailable(schema, (config) -> handler.accept(Objects.requireNonNull(getActiveConfig(configDataClass))));
        }
    }

    String DEFAULT_CONFIG_SCREEN_PROVIDER_ID = "default";
}
