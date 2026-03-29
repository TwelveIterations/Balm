package com.example.balm.neoforge.client;

import com.example.balm.client.BalmExampleClient;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = "balm_example", dist = Dist.CLIENT)
public class NeoForgeBalmExampleClient {

    public NeoForgeBalmExampleClient(ModContainer modContainer, IEventBus modEventBus) {
        BalmClient.initializeMod("balm_example", new NeoForgeLoadContext(modContainer, modEventBus), BalmExampleClient::initialize);
    }
}
