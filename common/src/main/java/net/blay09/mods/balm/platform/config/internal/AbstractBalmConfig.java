package net.blay09.mods.balm.platform.config.internal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.BalmConfig;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.blay09.mods.balm.platform.config.reflection.internal.ConfigReflection;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class AbstractBalmConfig implements BalmConfig {

    private static final List<String> DEFAULT_CONFIG_SCREEN_PROVIDERS = List.of("cloth-config", "configured", DEFAULT_CONFIG_SCREEN_PROVIDER_ID);

    private final Map<Identifier, BalmConfigSchema> schemas = new ConcurrentHashMap<>();
    private final Map<Identifier, MutableLoadedConfig> localConfigs = new ConcurrentHashMap<>();
    private final Map<Identifier, LoadedConfig> activeConfigs = new ConcurrentHashMap<>();
    private final Map<String, List<String>> configScreenProviderOrders = new ConcurrentHashMap<>();

    private final Map<Identifier, Object> activeReflectionConfigs = new ConcurrentHashMap<>();
    private final Multimap<Identifier, Consumer<MutableLoadedConfig>> configLoadHandlers = ArrayListMultimap.create();

    @Override
    public void registerConfig(BalmConfigSchema schema) {
        schemas.put(schema.identifier(), schema);
    }

    @Override
    public BalmConfigSchema getSchema(Identifier identifier) {
        return schemas.get(identifier);
    }

    @Override
    public MutableLoadedConfig getLocalConfig(Identifier identifier) {
        return localConfigs.get(identifier);
    }

    @Override
    public <T> void updateLocalConfig(Class<T> configDataClass, Consumer<T> updater) {
        final var schema = getSchema(configDataClass);
        if (schema == null) {
            throw new IllegalArgumentException("No config schema found for " + configDataClass.getName());
        }
        final var localConfig = getLocalConfig(schema);
        if (localConfig == null) {
            throw new IllegalArgumentException("No local config loaded for " + schema.identifier());
        }
        final var reflectionConfig = ConfigReflection.of(configDataClass, localConfig);
        updater.accept(reflectionConfig.data());
        saveLocalConfig(schema, reflectionConfig);
    }

    @Override
    public LoadedConfig getActiveConfig(Identifier identifier) {
        return activeConfigs.get(identifier);
    }

    @Override
    public Collection<BalmConfigSchema> getSchemasByNamespace(String namespace) {
        return schemas.values().stream().filter(schema -> schema.identifier().getNamespace().equals(namespace)).toList();
    }

    @Override
    public Collection<BalmConfigSchema> getSchemas() {
        return schemas.values();
    }

    @Override
    public void setPreferredConfigScreen(String modId, String providerId) {
        setPreferredConfigScreen(modId, List.of(providerId, DEFAULT_CONFIG_SCREEN_PROVIDER_ID));
    }

    @Override
    public void setPreferredConfigScreen(String modId, List<String> providerIds) {
        configScreenProviderOrders.put(modId, List.copyOf(providerIds));
    }

    @Override
    public List<String> getPreferredConfigScreenProviders(String modId) {
        return configScreenProviderOrders.getOrDefault(modId, List.of(DEFAULT_CONFIG_SCREEN_PROVIDER_ID));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getActiveConfig(Class<T> configDataClass) {
        final var identifier = ConfigReflection.getIdentifier(configDataClass);
        return (T) activeReflectionConfigs.computeIfAbsent(identifier, it -> BalmConfig.super.getActiveConfig(configDataClass));
    }

    @Override
    public void saveLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        activeReflectionConfigs.remove(schema.identifier());
        localConfigs.put(schema.identifier(), config);
        updateActiveFromLocal(schema, config);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void updateActiveFromLocal(BalmConfigSchema schema, MutableLoadedConfig config) {
        // Reapply active config from local config, but synced properties we will reset back to active values if connected to multiplayer
        final var newConfig = config.copy();
        if (Balm.safeClientAccess().isConnected() && !Balm.safeClientAccess().isLocalServer()) {
            final var activeConfig = activeConfigs.get(schema.identifier());
            for (final var rootProperty : schema.rootProperties()) {
                if (rootProperty.synced()) {
                    newConfig.setRaw((ConfiguredProperty) rootProperty, activeConfig.getRaw(rootProperty));
                }
            }
            for (final var category : schema.categories()) {
                for (final var property : category.properties()) {
                    if (property.synced()) {
                        newConfig.setRaw((ConfiguredProperty) property, activeConfig.getRaw(property));
                    }
                }
            }
        }
        setActiveConfig(schema, newConfig);
    }

    protected void setLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        localConfigs.put(schema.identifier(), config);
    }

    public void setActiveConfig(BalmConfigSchema schema, LoadedConfig config) {
        activeReflectionConfigs.remove(schema.identifier());
        activeConfigs.put(schema.identifier(), config);
    }

    public void resetToLocalConfig() {
        activeReflectionConfigs.clear();
        activeConfigs.putAll(localConfigs);
    }

    @Override
    public void onConfigAvailable(BalmConfigSchema schema, Consumer<MutableLoadedConfig> handler) {
        final var loaded = getLocalConfig(schema);
        if (loaded != null) {
            handler.accept(loaded);
        } else {
            synchronized (configLoadHandlers) {
                configLoadHandlers.put(schema.identifier(), handler);
            }
        }
    }

    protected void fireConfigLoadHandlers(BalmConfigSchema schema, MutableLoadedConfig config) {
        synchronized (configLoadHandlers) {
            configLoadHandlers.get(schema.identifier()).forEach(handler -> handler.accept(config));
        }
    }
}
