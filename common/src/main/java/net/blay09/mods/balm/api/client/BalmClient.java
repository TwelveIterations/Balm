package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalmClient {

    private static final BalmClientRuntime<BalmRuntimeLoadContext> runtime = BalmClientRuntimeSpi.create();
    private static final List<BalmClientModule> modules = Collections.synchronizedList(new ArrayList<>());

    public static void registerModule(BalmClientModule module) {
        modules.add(module);
        runtime.initializeModule(module);
    }

    public static <T extends BalmRuntimeLoadContext> void initialize(String modId, T context, Runnable initializer) {
        runtime.initialize(modId, context, initializer);
    }

    public static BalmRenderers getRenderers() {
        return runtime.getRenderers();
    }

    /**
     * @deprecated Use Kuma instead.
     */
    @Deprecated
    public static BalmKeyMappings getKeyMappings() {
        return runtime.getKeyMappings();
    }

    public static BalmScreens getScreens() {
        return runtime.getScreens();
    }

    public static BalmModels getModels() {
        return runtime.getModels();
    }
}
