package net.blay09.mods.balm.neoforge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.DisconnectedFromServerEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class NeoForgeBalmClient {
    public static void onInitializeClient(FMLClientSetupEvent setupEvent) {
        ((NeoForgeBalmClientRuntime) BalmClient.getRuntime()).initializeRuntime();
    }
}
