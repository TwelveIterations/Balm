package net.blay09.mods.balm.fabric.client.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.platform.internal.BalmClientSafeClientAccess;
import net.blay09.mods.balm.fabric.client.internal.platform.runtime.internal.FabricBalmClientRuntime;
import net.blay09.mods.balm.fabric.network.internal.FabricBalmNetworking;
import net.blay09.mods.balm.fabric.world.level.block.entity.internal.BlockEntityOnLoadCallback;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.network.internal.RemotePlayerModList;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.recipe.v1.sync.ClientRecipeSynchronizedEvent;
import net.minecraft.world.item.crafting.RecipeMap;

public class FabricBalmClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ((FabricBalmClientRuntime) BalmClient.getRuntime()).initializeRuntime();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> FabricBalmNetworking.initializeClientHandlers());

        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((client, level) -> BlockEntityOnLoadCallback.onClientLevelChanged(level));
        ClientPlayConnectionEvents.DISCONNECT.register((clientPacketListener, client) -> BlockEntityOnLoadCallback.onClientLevelChanged(null));

        ClientRecipeSynchronizedEvent.EVENT.register((client, synchronizedRecipes) -> {
            if (Balm.safeClientAccess() instanceof BalmClientSafeClientAccess clientProxy) {
                clientProxy.setSyncedRecipes(RecipeMap.create(synchronizedRecipes.recipes()));
            }
        });

        RemotePlayerModList.RECEIVED.register(event -> RemotePlayerModList.validateRemoteMods(event.player(), event.modList()));
    }
}
