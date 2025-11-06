package net.blay09.mods.balm.forge.loader;

import net.blay09.mods.balm.loader.ModInfo;
import net.minecraftforge.fml.ModContainer;

public class ForgeModInfo implements ModInfo {

    private final ModContainer modContainer;

    public ForgeModInfo(ModContainer modContainer) {
        this.modContainer = modContainer;
    }

    @Override
    public String name() {
        return modContainer.getModInfo().getDisplayName();
    }

    @Override
    public String versionString() {
        return modContainer.getModInfo().getVersion().toString();
    }
}
