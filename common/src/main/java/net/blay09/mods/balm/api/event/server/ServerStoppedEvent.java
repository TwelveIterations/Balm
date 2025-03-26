package net.blay09.mods.balm.api.event.server;

import net.minecraft.server.MinecraftServer;

public record ServerStoppedEvent(MinecraftServer server) {
}
