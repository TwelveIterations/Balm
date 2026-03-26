package net.blay09.mods.balm.neoforge.client.platform.config.internal;

import net.blay09.mods.balm.platform.config.internal.BalmConfigScreenProviders;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class NeoForgeBalmConfigScreenProviders {
    public static void initializeConfigurationScreen(ModContainer modContainer) {
        if (modContainer.getCustomExtension(IConfigScreenFactory.class).isEmpty()) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (targetModContainer, modListScreen) -> {
                final var factory = BalmConfigScreenProviders.getFactory(targetModContainer.getModId());
                if (factory != null) {
                    return factory.create(modListScreen);
                }

                return new ConfigurationScreen(targetModContainer, modListScreen);
            });
        }
    }
}
