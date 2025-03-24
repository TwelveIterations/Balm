package net.blay09.mods.balm.neoforge.config;

import com.google.common.collect.Table;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedTableConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public record LoadedNeoForgeConfig(BalmConfigSchema schema, ModConfig modConfig, Table<String, String, ModConfigSpec.ConfigValue<?>> properties) implements MutableLoadedConfig {

    @Override
    public <T> void setRaw(ConfiguredProperty<T> property, T value) {
        // TODO 1.21.5 Configs
    }

    @Override
    public MutableLoadedConfig copy() {
        final var newConfig = new LoadedTableConfig();
        newConfig.applyFrom(schema, newConfig);
        return newConfig;
    }

    @Override
    public <T> T getRaw(ConfiguredProperty<T> property) {
        return null; // TODO 1.21.5 Configs
    }

    @Override
    public MutableLoadedConfig mutable(BalmConfigSchema schema) {
        return this;
    }

}
