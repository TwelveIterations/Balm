package net.blay09.mods.balm.world;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;

public interface BalmMenuProvider<TPayload> extends MenuProvider {
    TPayload getScreenOpeningData(ServerPlayer player);

    StreamCodec<RegistryFriendlyByteBuf, TPayload> getScreenStreamCodec();
}
