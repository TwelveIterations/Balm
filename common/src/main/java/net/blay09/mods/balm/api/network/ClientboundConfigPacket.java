package net.blay09.mods.balm.api.network;

import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.LoadedTableConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record ClientboundConfigPacket(BalmConfigSchema schema, LoadedConfig config) implements CustomPacketPayload {

    public static final Type<ClientboundConfigPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("balm", "config"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundConfigPacket> STREAM_CODEC = StreamCodec.of(ClientboundConfigPacket::encode,
            ClientboundConfigPacket::decode);

    private static ClientboundConfigPacket decode(RegistryFriendlyByteBuf buf) {
        final var identifier = ResourceLocation.STREAM_CODEC.decode(buf);
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

    private static void encode(RegistryFriendlyByteBuf buf, ClientboundConfigPacket packet) {
        ResourceLocation.STREAM_CODEC.encode(buf, packet.schema.identifier());
        final var rootProperties = packet.schema.rootProperties();
        buf.writeVarInt(rootProperties.size());
        for (final var rootProperty : rootProperties) {
            buf.writeUtf(rootProperty.name());
            encodeProperty(rootProperty, buf, packet.config);
        }
        final var categories = packet.schema.categories();
        buf.writeVarInt(categories.size());
        for (final var category : categories) {
            buf.writeUtf(category.name());
            final var properties = category.properties();
            buf.writeVarInt(properties.size());
            for (final var property : properties) {
                buf.writeUtf(property.name());
                encodeProperty(property, buf, packet.config);
            }
        }
    }

    private static <T> void decodePropertyInto(ConfiguredProperty<T> property, ByteBuf buf, MutableLoadedConfig config) {
        final var value = property.streamCodec().decode(buf);
        config.setRaw(property, value);
    }

    private static <T> void encodeProperty(ConfiguredProperty<T> property, ByteBuf buf, LoadedConfig config) {
        property.streamCodec().encode(buf, config.getRaw(property));
    }

    public static void handle(Player player, ClientboundConfigPacket packet) {
        final var localConfig = Balm.getConfig().getLocalConfig(packet.schema);
        final var newConfig = localConfig.copy();
        newConfig.applyFrom(packet.schema, packet.config);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
