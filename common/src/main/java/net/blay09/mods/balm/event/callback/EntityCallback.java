package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface EntityCallback {
    void handle(Entity entity);

    EventMapper<EntityCallback> ADD = EventMapper.createUnbound("EntityCallback.ADD");
}
