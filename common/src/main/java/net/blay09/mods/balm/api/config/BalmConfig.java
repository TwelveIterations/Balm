package net.blay09.mods.balm.api.config;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.network.ConfigReflection;
import net.blay09.mods.balm.api.network.SyncConfigMessage;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public interface BalmConfig {
    @Deprecated(forRemoval = true, since = "1.21.5")
    private static BalmConfigProperty<?> createConfigProperty(BalmConfigData configData, Field categoryField, Field propertyField, BalmConfigData defaultConfig) {
        return new BalmConfigPropertyImpl<String>(configData, categoryField, propertyField, defaultConfig);
    }

    @Deprecated(forRemoval = true, since = "1.21.5")
    private static boolean isPropertyType(Class<?> type) {
        return type.isPrimitive()
                || type == String.class
                || type == Integer.class
                || type == Boolean.class
                || type == Float.class
                || type == Double.class
                || type == List.class
                || type == Set.class
                || type == ResourceLocationException.class
                || Enum.class.isAssignableFrom(type);
    }

    @Deprecated(forRemoval = true, since = "1.21.5")
    private static <T> T createConfigDataInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Config class or sub-class missing a public no-arg constructor.", e);
        }
    }

    void registerConfig(BalmConfigSchema schema);

    BalmConfigSchema getSchema(ResourceLocation identifier);

    MutableLoadedConfig getLocalConfig(ResourceLocation identifier);

    LoadedConfig getActiveConfig(ResourceLocation identifier);

    File getConfigDir();

    default File getConfigFile(BalmConfigSchema schema) {
        final var identifier = schema.identifier();
        return new File(getConfigDir(), identifier.getNamespace() + "-" + identifier.getPath() + ".toml");
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

    /**
     * @deprecated Use {@link #registerConfig(Class)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default <T extends BalmConfigData> T initializeBackingConfig(Class<T> clazz) {
        registerConfig(clazz);
        return getBackingConfig(clazz);
    }

    /**
     * @deprecated Use {@link #getLocalConfig(BalmConfigSchema)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default <T extends BalmConfigData> T getBackingConfig(Class<T> clazz) {
        final var schema = getSchema(clazz);
        final var localConfig = getLocalConfig(schema);
        final var reflectionConfig = ConfigReflection.of(clazz, localConfig);
        return reflectionConfig.data();
    }

    /**
     * @deprecated Use {@link #updateLocalConfig(Class, Consumer)} or {@link #saveLocalConfig(BalmConfigSchema)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default <T extends BalmConfigData> void saveBackingConfig(Class<T> clazz) {
        saveLocalConfig(getSchema(clazz));
    }

    /**
     * @deprecated Use {@link #getActiveConfig(Class)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default <T extends BalmConfigData> T getActive(Class<T> clazz) {
        return getActiveConfig(clazz);
    }

    /**
     * @deprecated Internal method. No-ops.
     */
    @Deprecated
    default <T extends BalmConfigData> void handleSync(Player player, SyncConfigMessage<T> message) {
    }

    /**
     * @deprecated Use {@link #registerConfig(Class)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default <T extends BalmConfigData> void registerConfig(Class<T> clazz, Function<T, SyncConfigMessage<T>> syncMessageFactory) {
        registerConfig(clazz);
    }

    /**
     * Use {@link #updateLocalConfig(Class, Consumer)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default <T extends BalmConfigData> void updateConfig(Class<T> clazz, Consumer<T> consumer) {
        updateLocalConfig(clazz, consumer);
    }

    /**
     * @deprecated Internal method. No-ops.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default <T extends BalmConfigData> void resetToBackingConfig(Class<T> clazz) {
    }

    /**
     * @deprecated Internal method. No-ops.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default void resetToBackingConfigs() {
    }

    /**
     * @deprecated Use {@link #getConfigFile(BalmConfigSchema)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default File getConfigFile(String configName) {
        return new File(getConfigDir(), configName + "-common.toml");
    }

    @Deprecated
    default <T extends BalmConfigData> Table<String, String, BalmConfigProperty<?>> getConfigProperties(Class<T> clazz) {
        var backingConfig = getBackingConfig(clazz);
        var defaultConfig = createConfigDataInstance(clazz);
        Table<String, String, BalmConfigProperty<?>> properties = HashBasedTable.create();
        for (Field rootField : ConfigReflection.getAllFields(clazz)) {
            var category = "";
            Class<?> fieldType = rootField.getType();
            if (isPropertyType(fieldType)) {
                var property = rootField.getName();
                properties.put(category, property, createConfigProperty(backingConfig, null, rootField, defaultConfig));
            } else {
                category = rootField.getName();
                for (Field propertyField : ConfigReflection.getAllFields(fieldType)) {
                    var property = propertyField.getName();
                    properties.put(category, property, createConfigProperty(backingConfig, rootField, propertyField, defaultConfig));
                }
            }
        }
        return properties;
    }

    /**
     * @deprecated Use {@link #getSchema(Class)} and {@link BalmConfigSchema#identifier()} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default <T extends BalmConfigData> String getConfigName(Class<T> clazz) {
        return ConfigReflection.getIdentifier(clazz).getNamespace();
    }

    /**
     * @deprecated Use {@link #getSchemasByNamespace(String)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default List<? extends BalmConfigData> getConfigsByMod(String modId) {
        return getSchemasByNamespace(modId).stream()
                .map(this::getActiveConfig)
                .filter(it -> it instanceof BalmConfigData)
                .map(it -> (BalmConfigData) it)
                .toList();
    }

}
