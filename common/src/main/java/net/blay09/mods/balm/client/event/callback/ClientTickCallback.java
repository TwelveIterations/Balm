package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.Minecraft;

@FunctionalInterface
public interface ClientTickCallback {
    void handle(Minecraft client);

    EventMapper<ClientTickCallback> PRE = EventMapper.createUnbound();
    EventMapper<ClientTickCallback> POST = EventMapper.createUnbound();
}
