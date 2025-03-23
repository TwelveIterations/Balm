package net.blay09.mods.balm.neoforge.config;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.common.config.AbstractBalmConfig;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;

public class NeoForgeBalmConfig extends AbstractBalmConfig {

    @Override
    public File getConfigDir() {
        return FMLPaths.CONFIGDIR.get().toFile();
    }

    @Override
    public void registerConfig(BalmConfigSchema schema) {
        super.registerConfig(schema);
        final var stringType = schema.identifier().getPath();

        // TODO 1.21.5: Configs
    }
}
