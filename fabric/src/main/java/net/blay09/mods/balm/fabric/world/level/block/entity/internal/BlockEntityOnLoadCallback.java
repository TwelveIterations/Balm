package net.blay09.mods.balm.fabric.world.level.block.entity.internal;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collection;

public interface BlockEntityOnLoadCallback {

    void balm$scheduleBlockEntityOnLoad(Collection<BlockEntity> blockEntities);
}
