package net.blay09.mods.balm.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;

public interface ServerLifecycleCallback {

    @FunctionalInterface
    interface Starting {
        void handle(MinecraftServer server);

        EventMapper<Starting> EVENT = EventMapper.createUnbound("ServerLifecycleCallback.Starting");
    }

    @FunctionalInterface
    interface Started {
        void handle(MinecraftServer server);

        EventMapper<Started> EVENT = EventMapper.createUnbound("ServerLifecycleCallback.Started");
    }

    @FunctionalInterface
    interface Stopping {
        void handle(MinecraftServer server);

        EventMapper<Stopping> EVENT = EventMapper.createUnbound("ServerLifecycleCallback.Stopping");
    }

    @FunctionalInterface
    interface Stopped {
        void handle(MinecraftServer server);

        EventMapper<Stopped> EVENT = EventMapper.createUnbound("ServerLifecycleCallback.Stopped");
    }

    @FunctionalInterface
    interface Reloading {
        void handle(MinecraftServer server, ReloadableServerResources resources);

        EventMapper<Reloading> EVENT = EventMapper.createUnbound("ServerLifecycleCallback.Reloading");
    }

    @FunctionalInterface
    interface Reloaded {
        void handle(MinecraftServer server);

        EventMapper<Reloaded> EVENT = EventMapper.createUnbound("ServerLifecycleCallback.Reloaded");
    }

}
