package net.blay09.mods.balm.fabric.platform.compatibility.modmenu.internal;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.blay09.mods.balm.Balm;
import org.jspecify.annotations.Nullable;

public class ModMenuUtils {
    @Nullable
    public static ConfigScreenFactory<?> getConfigScreen(String modId) {
        if (Balm.platform().isModLoaded("cloth-config")) {
            // TODO return ClothConfigUtils.getConfigScreen(modId);
        } else if (Balm.platform().isModLoaded("configured")) {
            // TODO return parent -> ConfiguredConfigProvider.createConfigScreen(modId, parent);
        }
        return null;
    }
}
