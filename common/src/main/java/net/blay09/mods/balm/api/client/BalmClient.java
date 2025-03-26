package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class BalmClient {
    private static final BalmClientRuntime runtime = BalmClientRuntimeSpi.create();

    public static void onRuntimeAvailable(Runnable callback) {
        runtime.onRuntimeAvailable(callback);
    }

    public static void initialize(String modId, Runnable initializer) {
        runtime.initialize(modId, initializer);
    }

    @Deprecated
    public static void initialize(String modId) {
        runtime.initialize(modId, () -> {
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


    public static BalmClientRuntime getRuntime() {
        return runtime;
    }
}
