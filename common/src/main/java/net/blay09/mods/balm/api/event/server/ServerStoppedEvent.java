package net.blay09.mods.balm.api.event.server;

import net.minecraft.server.MinecraftServer;

/**
 * @deprecated Use {@link net.blay09.mods.balm.event.callback.ServerLifecycleCallback#STOPPED} instead.
 */
@Deprecated
public record ServerStoppedEvent(MinecraftServer server) {
}
