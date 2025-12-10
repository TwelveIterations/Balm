package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.client.BalmClient;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ForgeBalmClient {
    public static void onInitializeClient(FMLClientSetupEvent setupEvent) {
        ((ForgeBalmClientRuntime) BalmClient.getRuntime()).initializeRuntime();
    }
}
