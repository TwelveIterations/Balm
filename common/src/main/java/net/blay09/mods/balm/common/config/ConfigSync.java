package net.blay09.mods.balm.common.config;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.v2.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.balm.api.event.client.DisconnectedFromServerEvent;
import net.blay09.mods.balm.api.network.ClientboundConfigPacket;

public class ConfigSync {
    public static void initialize() {
        Balm.getEvents().onEvent(PlayerLoginEvent.class, event -> {
            final var schemas = Balm.getConfig().getSchemas();
            for (final var schema : schemas) {
                if (hasSyncedProperties(schema)) {
                    final var loaded = Balm.getConfig().getActiveConfig(schema);
                    final var packet = new ClientboundConfigPacket(schema, loaded);
                    Balm.getNetworking().sendTo(event.getPlayer(), packet);
                }
            }
        });

        Balm.getEvents().onEvent(ConfigReloadedEvent.class, event -> {
            final var server = Balm.getHooks().getServer();
            if (server != null) {
                final var schemas = Balm.getConfig().getSchemas();
                for (final var schema : schemas) {
                    if (hasSyncedProperties(schema)) {
                        final var loaded = Balm.getConfig().getActiveConfig(schema);
                        final var packet = new ClientboundConfigPacket(schema, loaded);
                        Balm.getNetworking().sendToAll(server, packet);
                    }
                }
            }
        });

        Balm.getEvents().onEvent(DisconnectedFromServerEvent.class, event -> {
            final var config = Balm.getConfig();
            if (config instanceof AbstractBalmConfig abstractBalmConfig) {
                abstractBalmConfig.resetToLocalConfig();
            }
        });
    }

    private static boolean hasSyncedProperties(BalmConfigSchema schema) {
        return schema.rootProperties().stream().anyMatch(ConfiguredProperty::synced) || schema.categories()
                .stream()
                .flatMap(it -> it.properties().stream())
                .anyMatch(ConfiguredProperty::synced);
    }
}
