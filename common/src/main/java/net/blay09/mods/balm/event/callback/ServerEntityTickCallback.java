package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface ServerEntityTickCallback {
    void handle(Entity entity);

    EventMapper<ServerEntityTickCallback> PRE = EventMapper.createUnbound();
    EventMapper<ServerEntityTickCallback> POST = EventMapper.createUnbound();
}
