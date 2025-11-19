package net.blay09.mods.balm.neoforge.platform.internal;

import net.blay09.mods.balm.platform.ModInfo;
import net.neoforged.fml.ModContainer;

public class NeoForgeModInfo implements ModInfo {
    private final ModContainer modContainer;

    public NeoForgeModInfo(ModContainer modContainer) {
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
