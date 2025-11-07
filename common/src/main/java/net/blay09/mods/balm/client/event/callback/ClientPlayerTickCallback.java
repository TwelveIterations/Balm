package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.player.AbstractClientPlayer;

@FunctionalInterface
public interface ClientPlayerTickCallback {
    void handle(AbstractClientPlayer player);

    EventMapper<ClientPlayerTickCallback> PRE = EventMapper.createUnbound();
    EventMapper<ClientPlayerTickCallback> POST = EventMapper.createUnbound();
}
