package net.blay09.mods.balm.common.config;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.balm.api.network.ClientboundConfigSyncPacket;

public class ConfigSync {
    public static void initialize() {
        Balm.getEvents().onEvent(PlayerLoginEvent.class, event -> {
            final var schemas = Balm.getConfig().getSchemas();
            for (final var schema : schemas) {
                final var packet = new ClientboundConfigSyncPacket(schema);
                if (packet != null) {
                    Balm.getNetworking().sendTo(event.getPlayer(), packet);
                }
            }
        });

        Balm.getEvents().onEvent(ConfigReloadedEvent.class, event -> {
            final var server = Balm.getHooks().getServer();
            if (server != null) {
                final var schemas = Balm.getConfig().getSchemas();
                for (final var schema : schemas) {
                    ClientboundConfigSyncPacket packet = getConfigSyncMessage(config.getClass());
                    if (packet != null) {
                        Balm.getNetworking().sendToAll(server, packet);
                    }
                }
            }
        });
    }
}
