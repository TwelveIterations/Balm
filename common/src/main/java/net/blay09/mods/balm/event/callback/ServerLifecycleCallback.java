package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ServerLifecycleCallback {
    void handle(MinecraftServer server);

    EventMapper<ServerLifecycleCallback> STARTING = EventMapper.createUnbound();
    EventMapper<ServerLifecycleCallback> STARTED = EventMapper.createUnbound();
    EventMapper<ServerLifecycleCallback> STOPPING = EventMapper.createUnbound();
    EventMapper<ServerLifecycleCallback> STOPPED = EventMapper.createUnbound();
}
