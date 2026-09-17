package net.blay09.mods.balm.fabric.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collection;

public interface BlockEntityOnLoadCallback {

    void balm$scheduleBlockEntityOnLoad(Collection<BlockEntity> blockEntities);
}
