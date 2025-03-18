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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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
        if (this.getLevel() != null && !this.getLevel().isClientSide) {
            ((ServerLevel) this.getLevel()).getChunkSource().blockChanged(this.getBlockPos());
        }
    }

    public Packet<ClientGamePacketListener> createUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, (blockEntity, registryAccess) -> createUpdateTag(blockEntity));
    }

    public CompoundTag createUpdateTag(BlockEntity blockEntity) {
        final var tag = new CompoundTag();
        if (blockEntity instanceof BalmBlockEntity balmBlockEntity) {
            balmBlockEntity.writeUpdateTag(tag);
        }
        return tag;
    }

    protected void writeUpdateTag(CompoundTag tag) {
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (this instanceof BalmContainerProvider containerProvider) {
            if (level != null) {
                containerProvider.dropItems(level, pos);
            }
        }
    }
}
