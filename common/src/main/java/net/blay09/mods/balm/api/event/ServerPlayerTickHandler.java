package net.blay09.mods.balm.api.event;

import net.minecraft.server.level.ServerPlayer;

public interface ServerPlayerTickHandler {
    void handle(ServerPlayer player);
}
