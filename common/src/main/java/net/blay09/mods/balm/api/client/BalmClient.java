package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;

public class BalmClient {
    private static final BalmClientRuntime<BalmRuntimeLoadContext> runtime = BalmClientRuntimeSpi.create();

    public static void onRuntimeAvailable(Runnable callback) {
        runtime.onRuntimeAvailable(callback);
    }

    public static <T extends BalmRuntimeLoadContext> void initialize(String modId, T context, Runnable initializer) {
        runtime.initialize(modId, context, initializer);
    }

    public static BalmRenderers getRenderers() {
        return runtime.getRenderers();
    }

    /**
     * @deprecated No functionality left here.
     */
    @Deprecated
    public static BalmTextures getTextures() {
        return runtime.getTextures();
    }

    public static BalmKeyMappings getKeyMappings() {
        return runtime.getKeyMappings();
    }

    public static BalmScreens getScreens() {
        return runtime.getScreens();
    }

    public static BalmModels getModels() {
        return runtime.getModels();
    }


    public static BalmClientRuntime<? extends BalmRuntimeLoadContext> getRuntime() {
        return runtime;
    }
}
