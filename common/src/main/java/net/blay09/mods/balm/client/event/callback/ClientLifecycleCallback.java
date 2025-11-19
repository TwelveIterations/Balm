package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.Minecraft;

public interface ClientLifecycleCallback {
    @FunctionalInterface
    interface Started {
        void handle(Minecraft client);

        EventMapper<Started> EVENT = EventMapper.createUnbound("ClientLifecycleCallback.Started");
    }

    @FunctionalInterface
    interface ConnectedToServer {
        void handle(Minecraft client);

        EventMapper<ConnectedToServer> EVENT = EventMapper.createUnbound("ClientLifecycleCallback.ConnectedToServer");
    }

    @FunctionalInterface
    interface DisconnectedFromServer {
        void handle(Minecraft client);

        EventMapper<DisconnectedFromServer> EVENT = EventMapper.createUnbound("ClientLifecycleCallback.DisconnectedFromServer");
    }
}
