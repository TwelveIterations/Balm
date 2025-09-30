package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.block.entity.BalmBlockEntityBase;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

public class BalmBlockEntity extends BalmBlockEntityBase {

    public BalmBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return createUpdateTag(this);
    }

    @Override
    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return createUpdatePacket();
    }

    public void sync() {
        if (this.getLevel() != null && !this.getLevel().isClientSide()) {
            ((ServerLevel) this.getLevel()).getChunkSource().blockChanged(this.getBlockPos());
        }
    }

    public Packet<ClientGamePacketListener> createUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, (blockEntity, registryAccess) -> createUpdateTag(blockEntity));
    }

    public CompoundTag createUpdateTag(BlockEntity blockEntity) {
        final var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, blockEntity.getLevel().registryAccess());
        if (blockEntity instanceof BalmBlockEntity balmBlockEntity) {
            balmBlockEntity.writeUpdateTag(output);
        }
        return output.buildResult();
    }

    protected void writeUpdateTag(ValueOutput output) {
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (shouldDropContents(pos, state)) {
            super.preRemoveSideEffects(pos, state);
            dropContents(pos, state);
        }
    }

    protected boolean shouldDropContents(BlockPos pos, BlockState state) {
        return true;
    }

    protected void dropContents(BlockPos pos, BlockState state) {
        if (this instanceof BalmContainerProvider containerProvider && !(this instanceof Container)) {
            if (level != null) {
                containerProvider.dropItems(level, pos);
            }
        }
    }
}
