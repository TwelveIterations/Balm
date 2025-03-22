package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalmClient {

    private static final List<BalmClientModule> modules = Collections.synchronizedList(new ArrayList<>());
    private static BalmClientRuntime<BalmRuntimeLoadContext> runtime;

    public static void registerModule(BalmClientModule module) {
        modules.add(module);
        if (runtime != null) {
            runtime.initializeModule(module);
        }
    }

    public static <T extends BalmRuntimeLoadContext> void initialize(String modId, T context, Runnable initializer) {
        runtime.initialize(modId, context, initializer);
    }

    public static BalmRenderers getRenderers() {
        return requireRuntime().getRenderers();
    }

    /**
     * @deprecated No functionality left here.
     */
    @Deprecated
    public static BalmTextures getTextures() {
        return requireRuntime().getTextures();
    }

    public static BalmKeyMappings getKeyMappings() {
        return requireRuntime().getKeyMappings();
    }

    public static BalmScreens getScreens() {
        return requireRuntime().getScreens();
    }

    public static BalmModels getModels() {
        return requireRuntime().getModels();
    }


    public static BalmClientRuntime getRuntime() {
        return requireRuntime();
    }

    private static BalmClientRuntime requireRuntime() {
        if (runtime == null) {
            // TODO In 1.21.5, we will only initialize the runtime at a stable and safe time, and crash if accessed too early.
            initializeRuntime();
        }
        return runtime;
    }

    public static void initializeRuntime() {
        if (runtime == null) {
            runtime = BalmClientRuntimeSpi.create();
            for (final var module : modules) {
                runtime.initializeModule(module);
            }
        }
    }
}
