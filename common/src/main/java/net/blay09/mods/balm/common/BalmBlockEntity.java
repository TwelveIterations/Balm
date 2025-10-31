package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.block.entity.BalmBlockEntityBase;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.world.level.block.entity.BlockEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated Use {@link BlockEntity} with {@link BlockEntityUtils} instead, overriding {@link BlockEntity#getUpdateTag(HolderLookup.Provider)}, {@link BlockEntity#getUpdatePacket()} and {@link BlockEntity#preRemoveSideEffects(BlockPos, BlockState)} yourself.
 */
@Deprecated
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
        BlockEntityUtils.sync(this);
    }

    public Packet<ClientGamePacketListener> createUpdatePacket() {
        return BlockEntityUtils.createUpdatePacket(this);
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
