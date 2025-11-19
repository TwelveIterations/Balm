package net.blay09.mods.balm.common.config;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.client.event.callback.ClientLifecycleCallback;
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
