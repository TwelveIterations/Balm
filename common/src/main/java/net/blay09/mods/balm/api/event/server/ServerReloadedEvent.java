package net.blay09.mods.balm.api.event.server;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;

public class ServerReloadedEvent extends BalmEvent {
    private final MinecraftServer server;
    private final ReloadableServerResources resources;

    public ServerReloadedEvent(ReloadableServerResources resources) {
        this.server = Balm.getHooks().getServer();
        this.resources = resources;
    }

    public MinecraftServer getServer() {
        return server;
    }

    public ReloadableServerResources getResources() {
        return resources;
    }
}
