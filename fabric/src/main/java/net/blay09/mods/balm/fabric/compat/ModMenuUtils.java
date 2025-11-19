package net.blay09.mods.balm.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.blay09.mods.balm.api.Balm;

public class ModMenuUtils {
    public static ConfigScreenFactory<?> getConfigScreen(String modId) {
        if (Balm.platform().isModLoaded("cloth-config")) {
            return ClothConfigUtils.getConfigScreen(modId);
        } else if (Balm.platform().isModLoaded("configured")) {
            return parent -> ConfiguredConfigProvider.createConfigScreen(modId, parent);
        } else {
            return null;
        }
    }
}
