package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.Minecraft;

@FunctionalInterface
public interface ClientLifecycleCallback {
    void handle(Minecraft client);

    EventMapper<ClientLifecycleCallback> STARTED = EventMapper.createUnbound();
    EventMapper<ClientLifecycleCallback> CONNECTED_TO_SERVER = EventMapper.createUnbound();
    EventMapper<ClientLifecycleCallback> DISCONNECTED_FROM_SERVER = EventMapper.createUnbound();
}
