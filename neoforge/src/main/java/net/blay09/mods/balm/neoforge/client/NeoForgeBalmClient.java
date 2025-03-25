package net.blay09.mods.balm.neoforge.client;

import net.blay09.mods.balm.api.client.BalmClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = "balm", dist = Dist.CLIENT)
public class NeoForgeBalmClient {
    public NeoForgeBalmClient() {
        ((NeoForgeBalmClientRuntime) BalmClient.getRuntime()).initializeRuntime();
    }
}
