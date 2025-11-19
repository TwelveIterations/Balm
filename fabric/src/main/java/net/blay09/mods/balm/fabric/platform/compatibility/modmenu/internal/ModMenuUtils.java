package net.blay09.mods.balm.fabric.platform.compatibility.modmenu.internal;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.fabric.platform.compatibility.config.internal.ClothConfigUtils;
import net.blay09.mods.balm.fabric.platform.compatibility.config.internal.ConfiguredConfigProvider;

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
