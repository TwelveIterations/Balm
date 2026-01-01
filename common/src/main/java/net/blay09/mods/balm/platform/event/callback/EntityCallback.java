package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface EntityCallback {
    @FunctionalInterface
    interface AddedToLevel {
        void handle(Level level, Entity entity);

        EventMapper<AddedToLevel> EVENT = EventMapper.createUnbound("EntityCallback.AddedToLevel");
    }

    @FunctionalInterface
    interface RemovedFromLevel {
        void handle(Level level, Entity entity);

        EventMapper<RemovedFromLevel> EVENT = EventMapper.createUnbound("EntityCallback.RemovedFromLevel");
    }

    @FunctionalInterface
    interface DimensionChange {
        boolean allowDimensionChange(Entity entity, ResourceKey<Level> from, ResourceKey<Level> to);

        EventMapper<DimensionChange> BEFORE = EventMapper.createUnbound("EntityCallback.DimensionChange.Before");
    }
}
