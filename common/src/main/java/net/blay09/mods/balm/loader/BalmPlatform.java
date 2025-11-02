package net.blay09.mods.balm.loader;

import net.blay09.mods.balm.api.BalmEnvironment;

import java.util.List;

public interface BalmPlatform {
    boolean isModLoaded(String modId);

    String getModName(String modId);

    String name();

    BalmEnvironment physicalSide();

    boolean isDevelopmentEnvironment();

    List<String> loadedPrimaryModIds();
}
