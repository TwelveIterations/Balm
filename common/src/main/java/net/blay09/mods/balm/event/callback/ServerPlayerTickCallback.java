package net.blay09.mods.balm.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface ServerPlayerTickCallback {
    void handle(ServerPlayer player);

    EventMapper<ServerPlayerTickCallback> PRE = EventMapper.createUnbound();
    EventMapper<ServerPlayerTickCallback> POST = EventMapper.createUnbound();
}
