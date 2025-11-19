package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.network.NetworkVersions;
import net.blay09.mods.balm.api.network.ServerboundModListMessage;
import net.blay09.mods.balm.client.event.callback.ClientLifecycleCallback;
import net.blay09.mods.balm.fabric.network.FabricBalmNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import java.util.HashMap;

public class FabricBalmClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ((FabricBalmClientRuntime) BalmClient.getRuntime()).initializeRuntime();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> FabricBalmNetworking.initializeClientHandlers());

        ClientLifecycleCallback.ConnectedToServer.EVENT.register(client -> {
            final var networking = (FabricBalmNetworking) Balm.networking();
            final var modVersions = new HashMap<String, NetworkVersions>();
            for (final var modId : networking.getRegisteredMods()) {
                networking.getNetworkVersions(modId, BalmEnvironment.CLIENT)
                        .ifPresent(clientVersions -> modVersions.put(modId, clientVersions));
            }
            Balm.networking().sendToServer(new ServerboundModListMessage(modVersions));
        });
    }
}
