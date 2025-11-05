package net.blay09.mods.balm.world.level.block.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.function.Consumer;

public class BlockEntityUtils {
    public static void sync(BlockEntity blockEntity) {
        final var level = blockEntity.getLevel();
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().blockChanged(blockEntity.getBlockPos());
        }
    }

    public static Packet<ClientGamePacketListener> createUpdatePacket(BlockEntity blockEntity) {
        return ClientboundBlockEntityDataPacket.create(blockEntity, BlockEntity::getUpdateTag);
    }

    public CompoundTag createUpdateTag(BlockEntity blockEntity, Consumer<ValueOutput> outputConsumer) {
        final var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, blockEntity.getLevel().registryAccess());
        outputConsumer.accept(output);
        return output.buildResult();
    }
}
