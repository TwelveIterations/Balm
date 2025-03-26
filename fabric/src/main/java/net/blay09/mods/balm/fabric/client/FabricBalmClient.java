package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.ConnectedToServerEvent;
import net.blay09.mods.balm.api.network.NetworkVersions;
import net.blay09.mods.balm.api.network.ServerboundModListMessage;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmModels;
import net.blay09.mods.balm.fabric.network.FabricBalmNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

import java.util.HashMap;

public class FabricBalmClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ((FabricBalmClientRuntime) BalmClient.getRuntime()).initializeRuntime();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> FabricBalmNetworking.initializeClientHandlers());

        ModelLoadingPlugin.register((FabricBalmModels) BalmClient.getModels());

        Balm.getEvents().onEvent(ConnectedToServerEvent.class, event -> {
            final var networking = (FabricBalmNetworking) Balm.getNetworking();
            final var modVersions = new HashMap<String, NetworkVersions>();
            for (final var modId : networking.getRegisteredMods()) {
                networking.getNetworkVersions(modId).ifPresent(clientVersions -> {
                    if (!networking.isClientOnly(modId) && !networking.isServerOnly(modId)) {
                        modVersions.put(modId, clientVersions);
                    }
                });
            }
            Balm.getNetworking().sendToServer(new ServerboundModListMessage(modVersions));
        });
    }
}
