package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BalmClient {

    private static final Object RUNTIME_LOCK = new Object();
    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static volatile BalmClientRuntime runtime;

    public static void onRuntimeAvailable(Runnable callback) {
        initCallbacks.add(callback);
        synchronized (RUNTIME_LOCK) {
            if (runtime != null) {
                callback.run();
            }
        }
    }

    public static void initialize(String modId, Runnable initializer) {
        requireRuntime().initialize(modId, initializer);
    }

    @Deprecated
    public static void initialize(String modId) {
        requireRuntime().initialize(modId, () -> {
        });
    }

    /**
     * @deprecated Use Balm.getProxy() for extra side-safety
     */
    @Deprecated
    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
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
            synchronized (RUNTIME_LOCK) {
                if (runtime == null) { // intentional - first check is not synchronized for performance, but field may have changed by then
                    // TODO In 1.21.5, we will only initialize the runtime at a stable and safe time, and crash if accessed too early.
                    initializeRuntime();
                }
            }
        }
        return runtime;
    }

    public static void initializeRuntime() {
        synchronized (RUNTIME_LOCK) {
            if (runtime == null) {
                runtime = BalmClientRuntimeSpi.create();
                for (final var callback : initCallbacks) {
                    callback.run();
                }
            }
        }
    }
}
