package net.blay09.mods.balm.world;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface BalmMenuFactory<T extends AbstractContainerMenu, TPayload> {
    T create(int syncId, Inventory inventory, TPayload payload);

    StreamCodec<RegistryFriendlyByteBuf, TPayload> getStreamCodec();
}
