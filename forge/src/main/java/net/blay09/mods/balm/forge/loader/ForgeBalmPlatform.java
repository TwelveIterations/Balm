package net.blay09.mods.balm.forge.loader;

import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.proxy.LoaderPlatforms;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.minecraft.SharedConstants;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.List;

public class ForgeBalmPlatform implements BalmPlatform {
    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return ModList.get().getModContainerById(modId).map(it -> it.getModInfo().getDisplayName()).orElse(modId);
    }

    @Override
    public BalmEnvironment physicalSide() {
        return switch (FMLEnvironment.dist) {
            case CLIENT -> BalmEnvironment.CLIENT;
            case DEDICATED_SERVER -> BalmEnvironment.DEDICATED_SERVER;
        };
    }

    @Override
    public List<String> loadedPrimaryModIds() {
        return ModList.get().getMods().stream().map(IModInfo::getModId).toList();
    }

    @Override
    public String name() {
        return LoaderPlatforms.FORGE;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return SharedConstants.IS_RUNNING_IN_IDE;
    }

}
