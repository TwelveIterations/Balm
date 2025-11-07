package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.multiplayer.ClientLevel;

@FunctionalInterface
public interface ClientLevelTickCallback {
    void handle(ClientLevel level);

    EventMapper<ClientLevelTickCallback> PRE = EventMapper.createUnbound();
    EventMapper<ClientLevelTickCallback> POST = EventMapper.createUnbound();
}
