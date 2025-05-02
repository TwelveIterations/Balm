package net.blay09.mods.balm.common.config;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.network.ConfigReflection;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class AbstractBalmConfig implements BalmConfig {

    private final Map<ResourceLocation, BalmConfigSchema> schemas = new ConcurrentHashMap<>();
    private final Map<ResourceLocation, MutableLoadedConfig> localConfigs = new ConcurrentHashMap<>();
    private final Map<ResourceLocation, LoadedConfig> activeConfigs = new ConcurrentHashMap<>();

    private final Map<ResourceLocation, Object> activeReflectionConfigs = new ConcurrentHashMap<>();
    private final Multimap<ResourceLocation, Consumer<MutableLoadedConfig>> configLoadHandlers = ArrayListMultimap.create();

    @Override
    public void registerConfig(BalmConfigSchema schema) {
        schemas.put(schema.identifier(), schema);
    }

    @Override
    public BalmConfigSchema getSchema(ResourceLocation identifier) {
        return schemas.get(identifier);
    }

    @Override
    public MutableLoadedConfig getLocalConfig(ResourceLocation identifier) {
        return localConfigs.get(identifier);
    }

    @Override
    public <T> void updateLocalConfig(Class<T> configDataClass, Consumer<T> updater) {
        final var schema = getSchema(configDataClass);
        final var localConfig = getLocalConfig(schema);
        final var reflectionConfig = ConfigReflection.of(configDataClass, localConfig);
        updater.accept(reflectionConfig.data());
        saveLocalConfig(schema, reflectionConfig);
    }

    @Override
    public LoadedConfig getActiveConfig(ResourceLocation identifier) {
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
        if (Balm.getProxy().isConnected() && !Balm.getProxy().isLocalServer()) {
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
