package net.blay09.mods.balm.neoforge.client.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.client.platform.internal.BalmClientSafeClientAccess;
import net.blay09.mods.balm.neoforge.client.platform.runtime.internal.NeoForgeBalmClientRuntime;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = "balm", dist = Dist.CLIENT)
public class NeoForgeBalmClient {
    public NeoForgeBalmClient() {
        ((NeoForgeBalmClientRuntime) BalmClient.getRuntime()).initializeRuntime();

        NeoForge.EVENT_BUS.addListener(RecipesReceivedEvent.class, event -> {
            if (Balm.safeClientAccess() instanceof BalmClientSafeClientAccess clientProxy) {
                clientProxy.setSyncedRecipes(event.getRecipeMap());
            }
        });
    }
}
