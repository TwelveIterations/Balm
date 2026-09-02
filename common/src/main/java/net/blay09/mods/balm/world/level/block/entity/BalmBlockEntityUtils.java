package net.blay09.mods.balm.world.level.block.entity;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.capabilities.CommonCapabilities;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
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

    public static CompoundTag createUpdateTag(HolderLookup.Provider registries, Consumer<ValueOutput> outputConsumer) {
        final var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
        outputConsumer.accept(output);
        return output.buildResult();
    }

    public static LootContext getLootContext(ServerLevel level, BlockEntity blockEntity) {
        final var container = Balm.capabilities().getCapability(blockEntity, CommonCapabilities.CONTAINER);
        return (new LootContext.Builder((new LootParams.Builder(level))
                .withParameter(LootContextParams.BLOCK_STATE, blockEntity.getBlockState())
                .withParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockEntity.getBlockPos()))
                .withOptionalParameter(LootContextParams.CONTAINER, container)
                .create(LootContextParamSets.CONTAINER_PROCESS))).create(Optional.empty());
    }
}
