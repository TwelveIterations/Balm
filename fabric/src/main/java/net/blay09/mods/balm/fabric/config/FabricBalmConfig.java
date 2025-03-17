package net.blay09.mods.balm.fabric.config;

import com.mojang.logging.LogUtils;
import net.blay09.mods.balm.api.config.v2.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.v2.schema.BalmConfigSchema;
import net.blay09.mods.balm.common.config.AbstractBalmConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class FabricBalmConfig extends AbstractBalmConfig {

    private static final Logger logger = LogUtils.getLogger();

    /*@Override
    public <T extends BalmConfigHolder> T initializeBackingConfig(Class<T> clazz) {
        var configName = getConfigName(clazz);
        var configFile = getConfigFile(configName);
        var configData = createConfigDataInstance(clazz);
        if (configFile.exists()) {
            try {
                FabricConfigLoader.load(configFile, configData);
            } catch (IOException e) {
                logger.error("Failed to load config file {}", configFile, e);
            }
        } else {
            try {
                FabricConfigSaver.save(configFile, configData);
            } catch (IOException e) {
                logger.error("Failed to generate config file {}", configFile, e);
            }
        }
        configs.put(clazz, configData);
        configsByMod.put(configName, clazz);
        setActiveConfig(clazz, configData);
        return configData;
    }*/

    @Override
    public File getConfigDir() {
        return FabricLoader.getInstance().getConfigDir().toFile();
    }

    @Override
    public void saveLocalConfig(BalmConfigSchema schema, MutableLoadedConfig config) {
        final var configFile = new File(getConfigDir(), schema.identifier().getNamespace() + "-" + schema.identifier().getPath() + ".toml");
        try {
            FabricConfigSaver.save(configFile, schema, config);
        } catch (IOException e) {
            logger.error("Failed to save config file {}", configFile, e);
        }
    }

}
