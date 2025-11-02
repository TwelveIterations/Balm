package net.blay09.mods.balm.fabric.loader;

import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.proxy.LoaderPlatforms;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class FabricBalmPlatform implements BalmPlatform {
    @Override
    public BalmEnvironment physicalSide() {
        return switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> BalmEnvironment.CLIENT;
            case SERVER -> BalmEnvironment.DEDICATED_SERVER;
        };
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public List<String> loadedPrimaryModIds() {
        return FabricLoader.getInstance().getAllMods().stream()
                .map(it -> it.getMetadata().getId())
                .toList();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return FabricLoader.getInstance().getModContainer(modId).map(it -> it.getMetadata().getName()).orElse(modId);
    }

    @Override
    public String name() {
        return LoaderPlatforms.FABRIC;
    }
}
