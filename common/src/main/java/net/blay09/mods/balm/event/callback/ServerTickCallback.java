package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ServerTickCallback {
    void handle(MinecraftServer server);

    EventMapper<ServerTickCallback> PRE = EventMapper.createUnbound();
    EventMapper<ServerTickCallback> POST = EventMapper.createUnbound();
}
