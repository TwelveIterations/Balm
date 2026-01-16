package net.blay09.mods.balm.platform.module.internal;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public record ServerboundModListMessage(Map<String, NetworkVersions> modList) implements CustomPacketPayload {

    public static final Type<ServerboundModListMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath("balm", "mod_list"));
    
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundModListMessage> STREAM_CODEC = StreamCodec.of((buf, message) -> {
        buf.writeVarInt(message.modList().size());
        message.modList().forEach((modId, versions) -> {
            buf.writeUtf(modId);
            buf.writeUtf(versions.modVersion());
            buf.writeUtf(versions.networkVersion());
            buf.writeBoolean(versions.requireRemote());
        });
    }, (buf) -> {
        final var modVersions = new HashMap<String, NetworkVersions>();
        final var modCount = buf.readVarInt();
        for (int i = 0; i < modCount; i++) {
            final var modId = buf.readUtf();
            modVersions.put(modId, new NetworkVersions(buf.readUtf(), buf.readUtf(), buf.readBoolean()));
        }
        return new ServerboundModListMessage(modVersions);
    });

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
