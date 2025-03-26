package net.blay09.mods.balm.api.event;

import net.minecraft.server.MinecraftServer;

public interface ServerTickHandler {
    void handle(MinecraftServer server);
}
