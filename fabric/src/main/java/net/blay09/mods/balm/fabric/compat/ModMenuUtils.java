package net.blay09.mods.balm.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfigData;

public class ModMenuUtils {

    public static ConfigScreenFactory<?> getConfigScreen(String modId) {
        if (Balm.isModLoaded("cloth-config")) {
            return ClothConfigUtils.getConfigScreen(modId);
        } else if (Balm.isModLoaded("configured")) {
            return parent -> ConfiguredConfigProvider.createConfigScreen(modId, parent);
        } else {
            return null;
        }
    }

    /**
     * @deprecated Use {@link #getConfigScreen(String)} or remove your mod menu integration to fall back to Balm's default implementation.
     */
    @Deprecated(since = "1.21.5")
    public static <T extends BalmConfigData> ConfigScreenFactory<?> getConfigScreen(Class<T> clazz) {
        if (Balm.isModLoaded("cloth-config")) {
            return ClothConfigUtils.getConfigScreen(clazz);
        } else if (Balm.isModLoaded("configured")) {
            final var modId = Balm.getConfig().getConfigName(clazz);
            return parent -> ConfiguredConfigProvider.createConfigScreen(modId, parent);
        } else {
            return null;
        }
    }
}
