package net.blay09.mods.balm.api.event.server;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.server.MinecraftServer;

public class ServerStartedEvent extends BalmEvent {
    private final MinecraftServer server;

    public ServerStartedEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }
}
