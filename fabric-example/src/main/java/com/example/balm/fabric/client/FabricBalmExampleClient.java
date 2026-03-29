package com.example.balm.fabric.client;

import com.example.balm.client.BalmExampleClient;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.fabricmc.api.ClientModInitializer;

public class FabricBalmExampleClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BalmClient.initializeMod("balm_example", FabricLoadContext.INSTANCE, BalmExampleClient::initialize);
    }
}
