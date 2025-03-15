package net.blay09.mods.balm.api.network;

import net.blay09.mods.balm.api.config.v2.LoadedConfig;
import net.blay09.mods.balm.api.config.v2.schema.BalmConfigSchema;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class ClientboundConfigPacket implements CustomPacketPayload {

    public static final Type<ClientboundConfigPacket> type = new Type<>(ResourceLocation.fromNamespaceAndPath("balm", "config"));

    public ClientboundConfigPacket(BalmConfigSchema schema, LoadedConfig config) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return type;
    }
}
