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

    private static final Object RUNTIME_LOCK = new Object();
    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static final List<BalmClientModule> modules = Collections.synchronizedList(new ArrayList<>());
    private static volatile BalmClientRuntime<BalmRuntimeLoadContext> runtime;

    public static void registerModule(BalmClientModule module) {
        modules.add(module);
        synchronized (RUNTIME_LOCK) {
            if (runtime != null) {
                runtime.initializeModule(module);
            }
        }
    }

    public static void onRuntimeAvailable(Runnable callback) {
        initCallbacks.add(callback);
        synchronized (RUNTIME_LOCK) {
            if (runtime != null) {
                callback.run();
            }
        }
    }

    /**
     * @deprecated Use {@link #initializeMod(String, BalmRuntimeLoadContext, Runnable)} instead.
     */
    @Deprecated
    public static <T extends BalmRuntimeLoadContext> void initialize(String modId, T context, Runnable initializer) {
        initializeMod(modId, context, initializer);
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, Runnable initializer) {
        requireRuntime().initializeMod(modId, context, initializer);
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmClientModule module) {
        requireRuntime().initializeMod(modId, context, () -> registerModule(module));
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmClientModule... modules) {
        requireRuntime().initializeMod(modId, context, () -> {
            for (final var module : modules) {
                registerModule(module);
            }
        });
    }

    public static BalmRenderers getRenderers() {
        return requireRuntime().getRenderers();
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


    public static BalmClientRuntime<? extends BalmRuntimeLoadContext> getRuntime() {
        return requireRuntime();
    }

    @SuppressWarnings("unchecked")
    private static <T extends BalmRuntimeLoadContext> BalmClientRuntime<T> requireRuntime() {
        if (runtime == null) {
            synchronized (RUNTIME_LOCK) {
                if (runtime == null) { // intentional - first check is not synchronized for performance, but field may have changed by then
                    // TODO In 1.21.5, we will only initialize the runtime at a stable and safe time, and crash if accessed too early.
                    initializeRuntime();
                }
            }
        }
        return (BalmClientRuntime<T>) runtime;
    }

    public static void initializeRuntime() {
        synchronized (RUNTIME_LOCK) {
            if (runtime == null) {
                runtime = BalmClientRuntimeSpi.create();
                for (final var callback : initCallbacks) {
                    callback.run();
                }
                for (final var module : modules) {
                    runtime.initializeModule(module);
                }
            }
        }
    }
}
