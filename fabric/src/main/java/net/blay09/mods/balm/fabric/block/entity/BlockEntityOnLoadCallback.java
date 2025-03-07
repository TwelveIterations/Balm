package net.blay09.mods.balm.fabric.block.entity;

import net.blay09.mods.balm.api.block.entity.OnLoadHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BlockEntityOnLoadCallback {

    private static class LevelScope {
        private final List<BlockEntity> pendingFreshBlockEntities = new ArrayList<>();
        private final List<BlockEntity> freshBlockEntities = new ArrayList<>();
        private boolean onLoadRunning;
    }

    private static final Map<ResourceKey<Level>, LevelScope> levelBlockEntities = new ConcurrentHashMap<>();

    public static void scheduleOnLoad(Level level, Collection<BlockEntity> blockEntities) {
        final var scope = levelBlockEntities.computeIfAbsent(level.dimension(), (key) -> new LevelScope());
        if (scope.onLoadRunning) {
            scope.pendingFreshBlockEntities.addAll(blockEntities);
        } else {
            scope.freshBlockEntities.addAll(blockEntities);
        }
    }

    public static void fireOnLoad(Level level) {
        final var scope = levelBlockEntities.computeIfAbsent(level.dimension(), (key) -> new LevelScope());
        scope.freshBlockEntities.addAll(scope.pendingFreshBlockEntities);
        scope.pendingFreshBlockEntities.clear();

        scope.onLoadRunning = true;
        for (final var blockEntity : scope.freshBlockEntities) {
            if (blockEntity instanceof OnLoadHandler handler) {
                handler.onLoad();
            }
        }
        scope.freshBlockEntities.clear();
        scope.onLoadRunning = false;
    }
}
