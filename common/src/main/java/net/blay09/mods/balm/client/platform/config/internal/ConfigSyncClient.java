package net.blay09.mods.balm.client.platform.config.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.platform.module.BalmClientModule;
import net.blay09.mods.balm.client.platform.event.callback.ClientLifecycleCallback;
import net.blay09.mods.balm.platform.config.internal.AbstractBalmConfig;
import net.minecraft.resources.Identifier;

public class ConfigSyncClient implements BalmClientModule {

    @Override
    public Identifier getId() {
        return Identifier.fromNamespaceAndPath("balm", "config_sync_client");
    }

    @Override
    public void initialize() {
        ClientLifecycleCallback.DisconnectedFromServer.EVENT.register((client) -> {
            final var config = Balm.config();
            if (config instanceof AbstractBalmConfig abstractBalmConfig) {
                abstractBalmConfig.resetToLocalConfig();
            }
        });
    }

}
