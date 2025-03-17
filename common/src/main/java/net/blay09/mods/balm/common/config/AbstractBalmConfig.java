package net.blay09.mods.balm.common.config;

import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBalmConfig implements BalmConfig {

    private final Map<ResourceLocation, BalmConfigSchema> schemas = new HashMap<>();
    private final Map<ResourceLocation, MutableLoadedConfig> localConfigs = new HashMap<>();
    private final Map<ResourceLocation, LoadedConfig> activeConfigs = new HashMap<>();

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

    protected void setLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        localConfigs.put(schema.identifier(), config);
    }

    protected void setActiveConfig(BalmConfigSchema schema, LoadedConfig config) {
        activeConfigs.put(schema.identifier(), config);
    }

    public void resetToLocalConfig() {
        activeConfigs.putAll(localConfigs);
    }
}
