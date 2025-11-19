package net.blay09.mods.balm.neoforge.client.internal;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.neoforge.client.platform.runtime.internal.NeoForgeBalmClientRuntime;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = "balm", dist = Dist.CLIENT)
public class NeoForgeBalmClient {
    public NeoForgeBalmClient() {
        ((NeoForgeBalmClientRuntime) BalmClient.getRuntime()).initializeRuntime();
    }
}
