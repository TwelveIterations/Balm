package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class BalmClient {
    private static final BalmClientRuntime runtime = BalmClientRuntimeSpi.create();

    public static void registerModule(BalmClientModule module) {
        runtime.registerModule(module);
    }

    public static void onRuntimeAvailable(Runnable callback) {
        runtime.onRuntimeAvailable(callback);
    }

    /**
     * @deprecated Use {@link #initializeMod(String, BalmRuntimeLoadContext, Runnable)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.1")
    public static void initialize(String modId, Runnable initializer) {
        initializeMod(modId, EmptyLoadContext.INSTANCE, initializer);
    }

    /**
     * @deprecated Use {@link #initializeMod(String, BalmRuntimeLoadContext, Runnable)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.1")
    public static void initialize(String modId) {
        initialize(modId, () -> {});
    }

    /**
     * @deprecated Use Balm.getProxy() for extra side-safety
     */
    @Deprecated(forRemoval = true, since = "1.21.1")
    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    /**
     * @deprecated Use {@link #initializeMod(String, BalmRuntimeLoadContext, Runnable)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.22")
    public static <T extends BalmRuntimeLoadContext> void initialize(String modId, T context, Runnable initializer) {
        initializeMod(modId, context, initializer);
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, Runnable initializer) {
        runtime.initializeMod(modId, initializer);
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmClientModule module) {
        runtime.initializeMod(modId, () -> registerModule(module));
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmClientModule... modules) {
        runtime.initializeMod(modId, () -> {
            for (final var module : modules) {
                registerModule(module);
            }
        });
    }

    public static BalmRenderers getRenderers() {
        return runtime.getRenderers();
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


    public static BalmClientRuntime getRuntime() {
        return runtime;
    }

    /**
     * @deprecated No functionality left here.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    public static BalmTextures getTextures() {
        return runtime.getTextures();
    }
}
