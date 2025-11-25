package net.blay09.mods.balm.world.level.block.entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Consumer;

public class BalmBlockEntityUtils {
    public static void sync(BlockEntity blockEntity) {
        final var level = blockEntity.getLevel();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().blockChanged(blockEntity.getBlockPos());
        }
    }

    public static Packet<ClientGamePacketListener> createUpdatePacket(BlockEntity blockEntity) {
        return ClientboundBlockEntityDataPacket.create(blockEntity, BlockEntity::getUpdateTag);
    }

    public static CompoundTag createUpdateTag(HolderLookup.Provider registries, Consumer<CompoundTag> outputConsumer) {
        final var output = new CompoundTag();
        outputConsumer.accept(output);
        return output;
    }
}
