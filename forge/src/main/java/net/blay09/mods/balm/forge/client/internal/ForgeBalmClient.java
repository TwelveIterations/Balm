package net.blay09.mods.balm.forge.client.internal;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.forge.client.platform.runtime.internal.ForgeBalmClientRuntime;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ForgeBalmClient {
    public static void onInitializeClient(FMLClientSetupEvent setupEvent) {
        ((ForgeBalmClientRuntime) BalmClient.getRuntime()).initializeRuntime();
    }
}
