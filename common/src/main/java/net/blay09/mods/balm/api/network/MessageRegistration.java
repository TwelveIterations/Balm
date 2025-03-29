package net.blay09.mods.balm.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class MessageRegistration<TBuffer extends FriendlyByteBuf, TPayload extends CustomPacketPayload> {

    private final CustomPacketPayload.Type<TPayload> type;
    private final StreamCodec<TBuffer, TPayload> codec;

    public MessageRegistration(CustomPacketPayload.Type<TPayload> type, StreamCodec<TBuffer, TPayload> codec) {

        this.type = type;
        this.codec = codec;
    }

    public CustomPacketPayload.Type<TPayload> getType() {
        return type;
    }

    public StreamCodec<TBuffer, TPayload> getCodec() {
        return codec;
    }
}
