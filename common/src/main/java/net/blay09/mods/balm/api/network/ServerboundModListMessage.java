package net.blay09.mods.balm.api.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.Map;

public record ServerboundModListMessage(Map<String, NetworkVersions> modList) implements CustomPacketPayload {

    public static final Type<ServerboundModListMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath("balm", "mod_list"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
