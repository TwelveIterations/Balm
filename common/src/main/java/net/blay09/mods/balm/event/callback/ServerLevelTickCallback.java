package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface ServerLevelTickCallback {
    void handle(ServerLevel level);

    EventMapper<ServerLevelTickCallback> PRE = EventMapper.createUnbound();
    EventMapper<ServerLevelTickCallback> POST = EventMapper.createUnbound();
}
