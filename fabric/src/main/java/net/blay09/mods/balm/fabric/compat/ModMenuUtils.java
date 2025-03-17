package net.blay09.mods.balm.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;

public class ModMenuUtils {
    public static <T> ConfigScreenFactory<?> getConfigScreen(BalmConfigSchema schema) {
        if (Balm.isModLoaded("cloth-config")) {
            return ClothConfigUtils.getConfigScreen(schema);
        } else if (Balm.isModLoaded("configured")) {
            return parent -> ConfiguredConfigProvider.createConfigScreen(schema.identifier().getNamespace(), parent);
        } else {
            return null;
        }
    }
}
