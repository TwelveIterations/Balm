package net.blay09.mods.balm.fabric.platform.compatibility.modmenu.internal;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.blay09.mods.balm.platform.config.internal.BalmConfigScreenProviders;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        final var result = new HashMap<String, ConfigScreenFactory<?>>();
        for (final var modId : BalmConfigScreenProviders.getConfigurableModIds()) {
            final var screenFactory = BalmConfigScreenProviders.getFactory(modId);
            if (screenFactory != null) {
                result.put(modId, screenFactory::create);
            }
        }
        return result;
    }
}
