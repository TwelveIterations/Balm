package net.blay09.mods.balm.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ServerboundMessageRegistration<T> extends MessageRegistration<T> {

    private final BiConsumer<ServerPlayer, T> handler;

    public ServerboundMessageRegistration(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<ServerPlayer, T> handler) {
        super(identifier, clazz, encodeFunc, decodeFunc);
        this.handler = handler;
    }

    public BiConsumer<ServerPlayer, T> getHandler() {
        return handler;
    }
}
