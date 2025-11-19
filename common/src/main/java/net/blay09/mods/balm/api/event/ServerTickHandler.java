package net.blay09.mods.balm.api.event;

import net.minecraft.server.MinecraftServer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerTickCallback} instead.
 */
@Deprecated
public interface ServerTickHandler {
    void handle(MinecraftServer server);
}
