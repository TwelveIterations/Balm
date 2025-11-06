package net.blay09.mods.balm.fabric.loader;

import net.blay09.mods.balm.loader.ModInfo;
import net.fabricmc.loader.api.ModContainer;

public class FabricModInfo implements ModInfo {
    private final ModContainer modContainer;

    public FabricModInfo(ModContainer modContainer) {
        this.modContainer = modContainer;
    }

    @Override
    public String name() {
        return modContainer.getMetadata().getName();
    }

    @Override
    public String versionString() {
        return modContainer.getMetadata().getVersion().toString();
    }
}
