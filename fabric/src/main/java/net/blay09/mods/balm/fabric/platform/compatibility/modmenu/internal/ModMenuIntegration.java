package net.blay09.mods.balm.fabric.platform.compatibility.modmenu.internal;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.blay09.mods.balm.Balm;
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
        for (final var schema : Balm.config().getSchemas()) {
            final var namespace = schema.identifier().getNamespace();
            final var screenFactory = BalmConfigScreenProviders.getFactory(namespace);
            if (screenFactory != null) {
                result.put(namespace, screenFactory::create);
            }
        }
        for (final var entry : BalmConfigScreenProviders.getModOverrides()) {
            result.put(entry.getKey(), entry.getValue()::create);
        }
        return result;
    }
}
