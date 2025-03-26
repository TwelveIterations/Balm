package net.blay09.mods.balm.api.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ClientboundMessageRegistration<T> extends MessageRegistration<T> {

    private final BiConsumer<Player, T> handler;

    public ClientboundMessageRegistration(ResourceLocation identifier, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encodeFunc, Function<FriendlyByteBuf, T> decodeFunc, BiConsumer<Player, T> handler) {
        super(identifier, clazz, encodeFunc, decodeFunc);
        this.handler = handler;
    }

    public BiConsumer<Player, T> getHandler() {
        return handler;
    }
}
