package net.blay09.mods.balm.common.config;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.balm.api.event.PlayerLoginEvent;
import net.blay09.mods.balm.api.event.client.DisconnectedFromServerEvent;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.network.ClientboundConfigPacket;
import net.minecraft.resources.ResourceLocation;

public class ConfigSync implements BalmModule {

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath("balm", "config_sync");
    }

    @Override
    public void registerNetworking(BalmNetworking networking) {
        networking.registerClientboundPacket(ClientboundConfigPacket.TYPE,
                ClientboundConfigPacket.class,
                ClientboundConfigPacket.STREAM_CODEC,
                ClientboundConfigPacket::handle);
    }

    @Override
    public void registerEvents(BalmEvents events) {
        events.onEvent(PlayerLoginEvent.class, event -> {
            final var schemas = Balm.getConfig().getSchemas();
            for (final var schema : schemas) {
                if (hasSyncedProperties(schema)) {
                    final var loaded = Balm.getConfig().getActiveConfig(schema);
                    final var packet = new ClientboundConfigPacket(schema, loaded);
                    Balm.getNetworking().sendTo(event.getPlayer(), packet);
                }
            }
        });

        events.onEvent(ConfigReloadedEvent.class, event -> {
            final var server = Balm.getHooks().getServer();
            if (server != null) {
                final var schema = event.getSchema();
                if (schema != null && hasSyncedProperties(schema)) {
                    final var loaded = Balm.getConfig().getActiveConfig(schema);
                    final var packet = new ClientboundConfigPacket(schema, loaded);
                    Balm.getNetworking().sendToAll(server, packet);
                }
            }
        });

        events.onEvent(DisconnectedFromServerEvent.class, event -> {
            final var config = Balm.getConfig();
            if (config instanceof AbstractBalmConfig abstractBalmConfig) {
                abstractBalmConfig.resetToLocalConfig();
            }
        });
    }

    public static boolean hasSyncedProperties(BalmConfigSchema schema) {
        return schema.rootProperties().stream().anyMatch(ConfiguredProperty::synced) || schema.categories()
                .stream()
                .anyMatch(ConfigSync::hasSyncedProperties);
    }

    public static boolean hasSyncedProperties(ConfigCategory category) {
        return category.properties().stream().anyMatch(ConfiguredProperty::synced);
    }
}
