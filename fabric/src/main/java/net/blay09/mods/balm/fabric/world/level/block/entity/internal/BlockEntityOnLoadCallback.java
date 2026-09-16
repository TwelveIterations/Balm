package net.blay09.mods.balm.fabric.world.level.block.entity.internal;

import net.blay09.mods.balm.world.level.block.entity.OnLoadHandler;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class BlockEntityOnLoadCallback {

    private static final Map<Level, LevelScope> levelBlockEntities = new WeakHashMap<>();

    public static void scheduleOnLoad(Level level, Collection<BlockEntity> blockEntities) {
        synchronized (levelBlockEntities) {
            final var scope = levelBlockEntities.computeIfAbsent(level, (key) -> new LevelScope());
            if (scope.onLoadRunning) {
                scope.pendingFreshBlockEntities.addAll(blockEntities);
            } else {
                scope.freshBlockEntities.addAll(blockEntities);
            }
        }
    }

    public static void fireOnLoad(Level level) {
        synchronized (levelBlockEntities) {
            final var scope = levelBlockEntities.computeIfAbsent(level, (key) -> new LevelScope());
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

    public static void onClientLevelChanged(@Nullable Level newLevel) {
        synchronized (levelBlockEntities) {
            // The client only has one level at a time, so any other client level in here is gone for good. Its scope has to be
            // removed explicitly since the block entities it holds reference the level, which keeps the weak key alive.
            levelBlockEntities.keySet().removeIf(level -> level.isClientSide() && level != newLevel);
        }
    }

    private static class LevelScope {
        private final List<BlockEntity> pendingFreshBlockEntities = new ArrayList<>();
        private final List<BlockEntity> freshBlockEntities = new ArrayList<>();
        private boolean onLoadRunning;
    }
}
