package net.blay09.mods.balm.api.network;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.LoadedTableConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.blay09.mods.balm.common.codec.ByteBufCodecs;
import net.blay09.mods.balm.common.config.AbstractBalmConfig;
import net.blay09.mods.balm.common.config.ConfigSync;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public record ClientboundConfigPacket(BalmConfigSchema schema, LoadedConfig config) {

    public static ClientboundConfigPacket decode(FriendlyByteBuf buf) {
        final var identifier = ByteBufCodecs.RESOURCE_LOCATION.decode(buf);
        final var schema = Balm.getConfig().getSchema(identifier);
        final var config = new LoadedTableConfig();
        final var rootPropertyCount = buf.readVarInt();
        for (int j = 0; j < rootPropertyCount; j++) {
            final var property = buf.readUtf();
            final var propertySchema = schema.findRootProperty(property);
            decodePropertyInto(propertySchema, buf, config);
        }
        final var categoryCount = buf.readVarInt();
        for (int i = 0; i < categoryCount; i++) {
            final var category = buf.readUtf();
            final var propertyCount = buf.readVarInt();
            for (int j = 0; j < propertyCount; j++) {
                final var property = buf.readUtf();
                final var propertySchema = schema.findProperty(category, property);
                decodePropertyInto(propertySchema, buf, config);
            }
        }
        return new ClientboundConfigPacket(schema, config);
    }

    public static void encode(ClientboundConfigPacket packet, FriendlyByteBuf buf) {
        ByteBufCodecs.RESOURCE_LOCATION.encode(buf, packet.schema.identifier());
        final var rootProperties = packet.schema.rootProperties().stream().filter(ConfiguredProperty::synced).toList();
        buf.writeVarInt(rootProperties.size());
        for (final var rootProperty : rootProperties) {
            buf.writeUtf(rootProperty.name());
            encodeProperty(rootProperty, buf, packet.config);
        }
        final var categories = packet.schema.categories().stream().filter(ConfigSync::hasSyncedProperties).toList();
        buf.writeVarInt(categories.size());
        for (final var category : categories) {
            buf.writeUtf(category.name());
            final var properties = category.properties().stream().filter(ConfiguredProperty::synced).toList();
            buf.writeVarInt(properties.size());
            for (final var property : properties) {
                buf.writeUtf(property.name());
                encodeProperty(property, buf, packet.config);
            }
        }
    }

    private static <T> void decodePropertyInto(ConfiguredProperty<T> property, FriendlyByteBuf buf, MutableLoadedConfig config) {
        final var value = property.streamCodec().decode(buf);
        config.setRaw(property, value);
    }

    private static <T> void encodeProperty(ConfiguredProperty<T> property, FriendlyByteBuf buf, LoadedConfig config) {
        final var value = config.getRaw(property);
        property.streamCodec().encode(buf, value);
    }

    public static void handle(Player player, ClientboundConfigPacket packet) {
        final var localConfig = Balm.getConfig().getLocalConfig(packet.schema);
        final var newConfig = localConfig.copy();
        newConfig.applyFrom(packet.schema, packet.config);
        if (Balm.getConfig() instanceof AbstractBalmConfig config) {
            config.setActiveConfig(packet.schema, newConfig);
        }
    }

}
