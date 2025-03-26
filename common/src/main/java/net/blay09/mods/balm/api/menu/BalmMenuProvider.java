package net.blay09.mods.balm.api.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface BalmMenuProvider extends MenuProvider {
    default void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
        if (this instanceof BlockEntity blockEntity) {
            buf.writeBlockPos(blockEntity.getBlockPos());
        }
    }
}
