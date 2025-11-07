package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface ClientEntityTickCallback {
    void handle(Entity entity);

    EventMapper<ClientEntityTickCallback> PRE = EventMapper.createUnbound();
    EventMapper<ClientEntityTickCallback> POST = EventMapper.createUnbound();
}
