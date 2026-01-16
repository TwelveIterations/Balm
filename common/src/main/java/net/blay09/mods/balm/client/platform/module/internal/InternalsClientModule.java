package net.blay09.mods.balm.client.platform.module.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.client.platform.event.callback.ClientLifecycleCallback;
import net.blay09.mods.balm.client.platform.module.BalmClientModule;
import net.blay09.mods.balm.network.internal.CommonBalmNetworking;
import net.blay09.mods.balm.platform.BalmEnvironment;
import net.blay09.mods.balm.platform.module.internal.NetworkVersions;
import net.blay09.mods.balm.platform.module.internal.ServerboundModListMessage;
import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.minecraft.resources.Identifier;

import java.util.HashMap;

/**
 * Internal module that registers Balm's own functionality.
 * Use {@link BalmClientModule} for your own modules.
 *
 * @see BalmClientModule
 * @see BalmClient#initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)
 */
public final class InternalsClientModule implements BalmClientModule {
    private static final String MOD_ID = "balm";

    @Override
    public Identifier getId() {
        return Identifier.fromNamespaceAndPath(MOD_ID, "client");
    }

    @Override
    public void initialize() {
        ClientLifecycleCallback.ConnectedToServer.EVENT.register(client -> {
            final var networking = (CommonBalmNetworking) Balm.networking();
            final var modVersions = new HashMap<String, NetworkVersions>();
            for (final var modId : networking.getRegisteredMods()) {
                networking.getNetworkVersions(modId, BalmEnvironment.CLIENT)
                        .ifPresent(clientVersions -> modVersions.put(modId, clientVersions));
            }
            Balm.networking().sendToServer(new ServerboundModListMessage(modVersions));
        });
    }
}
