package net.blay09.mods.balm.fabric.platform.config.internal;

import com.mojang.logging.LogUtils;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.blay09.mods.balm.platform.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.platform.config.internal.AbstractBalmConfig;
import net.blay09.mods.balm.platform.event.callback.ServerLifecycleCallback;
import net.blay09.mods.balm.fabric.platform.event.internal.FabricBalmSupplementalEvents;
import net.blay09.mods.balm.platform.event.internal.BalmSupplementalEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class FabricBalmConfig extends AbstractBalmConfig {

    private static final Logger logger = LogUtils.getLogger();
    private final AtomicReference<@Nullable MinecraftServer> currentServer = new AtomicReference<>();

    @Override
    public void registerConfig(BalmConfigSchema schema) {
        super.registerConfig(schema);

        if (!isServerScoped(schema)) {
            loadLocalConfig(schema);
        } else {
            final var defaultConfig = schema.defaults().mutable(schema);
            setLocalConfig(schema, defaultConfig);
            setActiveConfig(schema, defaultConfig);

            // Not pretty, but we don't have a config load context yet.
            // Might revisit after the event overhaul and perhaps add more context to BalmConfig in 1.21.11.
            ServerLifecycleCallback.Starting.EVENT.register(server -> {
                currentServer.set(server);
                loadLocalConfig(schema);
            });
            ServerLifecycleCallback.Stopped.EVENT.register(server -> currentServer.set(null));
        }
    }

    private void loadLocalConfig(BalmConfigSchema schema) {
        final var config = loadConfigFromConfigFile(schema);
        final var mutableConfig = config.mutable(schema);
        setLocalConfig(schema, mutableConfig);
        setActiveConfig(schema, config);
        fireConfigLoadHandlers(schema, mutableConfig);
        BalmSupplementalEvents.CONFIG_LOADED.invoker().handle(schema);
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
        BalmSupplementalEvents.CONFIG_RELOADED.invoker().handle(schema);
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

    @Override
    public File getConfigDir(BalmConfigSchema schema) {
        // Match Neo/Forge for configs of type "server"
        if (isServerScoped(schema)) {
            final var server = currentServer.get();
            if (server != null) {
                return server.getWorldPath(LevelResource.ROOT).resolve("serverconfig").toFile();
            } else {
                throw new IllegalStateException("Cannot get config file for " + schema + " without a server running.");
            }
        }
        return super.getConfigDir(schema);
    }

    private boolean isServerScoped(BalmConfigSchema schema) {
        return schema.identifier().getPath().equals("server");
    }
}
