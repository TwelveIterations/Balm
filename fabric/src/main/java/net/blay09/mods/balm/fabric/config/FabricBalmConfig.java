package net.blay09.mods.balm.fabric.config;

import com.mojang.logging.LogUtils;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.event.ConfigLoadedEvent;
import net.blay09.mods.balm.common.config.AbstractBalmConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class FabricBalmConfig extends AbstractBalmConfig {

    private static final Logger logger = LogUtils.getLogger();

    @Override
    public void registerConfig(BalmConfigSchema schema) {
        super.registerConfig(schema);
        final var config = loadConfigFromConfigFile(schema);
        final var mutableConfig = config.mutable(schema);
        setLocalConfig(schema, mutableConfig);
        setActiveConfig(schema, config);
        fireConfigLoadHandlers(schema, mutableConfig);
        Balm.getEvents().fireEvent(new ConfigLoadedEvent(schema));
    }

    @Override
    public File getConfigDir() {
        return FabricLoader.getInstance().getConfigDir().toFile();
    }

    @Override
    public void saveLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        super.saveLocalConfig(schema, config);
        final var configFile = getConfigFile(schema);
        try {
            FabricConfigSaver.save(configFile, schema, config);
        } catch (IOException e) {
            logger.error("Failed to save config file {}", configFile, e);
        }
    }

    private LoadedConfig loadConfigFromConfigFile(BalmConfigSchema schema) {
        final var configFile = getConfigFile(schema);
        LoadedConfig config = schema.defaults();
        if (configFile.exists()) {
            try {
                config = FabricConfigLoader.load(configFile, schema);
            } catch (IOException e) {
                logger.error("Failed to load config file {}", configFile, e);
            }
        } else {
            try {
                FabricConfigSaver.save(configFile, schema, schema.defaults());
            } catch (IOException e) {
                logger.error("Failed to generate config file {}", configFile, e);
            }
        }
        return config;
    }

}
