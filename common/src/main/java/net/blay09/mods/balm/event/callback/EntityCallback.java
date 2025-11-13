package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public interface EntityCallback {
    @FunctionalInterface
    interface Add {
        void handle(Level level, Entity entity);

        EventMapper<Add> EVENT = EventMapper.createUnbound("EntityCallback.Add");
    }
}
