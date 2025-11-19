package net.blay09.mods.balm.common.config;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.api.config.schema.builder.ConfigCategory;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.network.ClientboundConfigPacket;
import net.blay09.mods.balm.event.callback.ConfigCallback;
import net.blay09.mods.balm.event.callback.ServerPlayerCallback;
import net.minecraft.resources.Identifier;

public class ConfigSync implements BalmModule {

    public static boolean hasImplicitSync(BalmConfigSchema schema) {
        return schema.identifier().getPath().equals("server");
    }

    public static boolean hasSyncedProperties(BalmConfigSchema schema) {
        return hasImplicitSync(schema)
                || schema.rootProperties().stream().anyMatch(ConfiguredProperty::synced)
                || schema.categories().stream().anyMatch(ConfigSync::hasSyncedProperties);
    }

    public static boolean hasSyncedProperties(ConfigCategory category) {
        return category.properties().stream().anyMatch(ConfiguredProperty::synced);
    }

    @Override
    public Identifier getId() {
        return Identifier.fromNamespaceAndPath("balm", "config_sync");
    }

    @Override
    public void registerNetworking(BalmNetworking networking) {
        networking.registerClientboundPacket(ClientboundConfigPacket.TYPE,
                ClientboundConfigPacket.class,
                ClientboundConfigPacket.STREAM_CODEC,
                ClientboundConfigPacket::handle);
    }

    @Override
    public void initialize() {
        ServerPlayerCallback.Login.EVENT.register(player -> {
            final var schemas = Balm.config().getSchemas();
            for (final var schema : schemas) {
                if (hasSyncedProperties(schema)) {
                    final var loaded = Balm.config().getActiveConfig(schema);
                    if (loaded != null) {
                        final var packet = new ClientboundConfigPacket(schema, loaded);
                        Balm.networking().sendTo(player, packet);
                    }
                }
            }
        });

        ConfigCallback.Reloaded.EVENT.register(schema -> {
            final var server = Balm.platform().server();
            if (server != null) {
                if (hasSyncedProperties(schema)) {
                    final var loaded = Balm.config().getActiveConfig(schema);
                    if (loaded != null) {
                        final var packet = new ClientboundConfigPacket(schema, loaded);
                        Balm.networking().sendToAll(server, packet);
                    }
                }
            }
        });
    }

    public static boolean isSyncedProperty(ConfiguredProperty<?> configuredProperty) {
        return hasImplicitSync(configuredProperty.parentSchema()) || configuredProperty.synced();
    }
}
