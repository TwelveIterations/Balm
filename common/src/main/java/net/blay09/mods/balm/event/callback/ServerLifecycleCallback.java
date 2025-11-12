package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface ServerLifecycleCallback {
    void handle(MinecraftServer server);

    EventMapper<ServerLifecycleCallback> STARTING = EventMapper.createUnbound("ServerLifecycleCallback.STARTING");
    EventMapper<ServerLifecycleCallback> STARTED = EventMapper.createUnbound("ServerLifecycleCallback.STARTED");
    EventMapper<ServerLifecycleCallback> STOPPING = EventMapper.createUnbound("ServerLifecycleCallback.STOPPING");
    EventMapper<ServerLifecycleCallback> STOPPED = EventMapper.createUnbound("ServerLifecycleCallback.STOPPED");
    EventMapper<ServerLifecycleCallback> RELOADED = EventMapper.createUnbound("ServerLifecycleCallback.RELOADED");
}
