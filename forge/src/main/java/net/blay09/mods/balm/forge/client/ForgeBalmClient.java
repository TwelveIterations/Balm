package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.DisconnectedFromServerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ForgeBalmClient {
    public static void onInitializeClient(FMLClientSetupEvent setupEvent) {
        BalmClient.initializeRuntime();

        Balm.getEvents().onEvent(DisconnectedFromServerEvent.class, event -> Balm.getConfig().resetToBackingConfigs());
    }
}
