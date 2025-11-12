package net.blay09.mods.balm.api.event.server;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.server.MinecraftServer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerLifecycleCallback#RELOADED} instead.
 */
@Deprecated
public class ServerReloadFinishedEvent extends BalmEvent {
    private final MinecraftServer server;

    public ServerReloadFinishedEvent(MinecraftServer server) {
        this.server = server;
    }

    public MinecraftServer getServer() {
        return server;
    }
}
