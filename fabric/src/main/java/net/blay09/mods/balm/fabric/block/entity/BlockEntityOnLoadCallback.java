package net.blay09.mods.balm.fabric.block.entity;

import net.blay09.mods.balm.api.block.entity.OnLoadHandler;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlockEntityOnLoadCallback {

    private static final List<BlockEntity> pendingFreshBlockEntities = new ArrayList<>();
    private static final List<BlockEntity> freshBlockEntities = new ArrayList<>();
    private static boolean callbacksRunning = false;

    public static synchronized void scheduleOnLoad(Collection<BlockEntity> blockEntities) {
        if (callbacksRunning) {
            pendingFreshBlockEntities.addAll(blockEntities);
        } else {
            freshBlockEntities.addAll(blockEntities);
        }
    }

    public static synchronized void fireOnLoad() {
        freshBlockEntities.addAll(pendingFreshBlockEntities);
        pendingFreshBlockEntities.clear();

        callbacksRunning = true;
        for (final var blockEntity : freshBlockEntities) {
            if (blockEntity instanceof OnLoadHandler handler) {
                handler.onLoad();
            }
        }
        freshBlockEntities.clear();
        callbacksRunning = false;
    }
}
