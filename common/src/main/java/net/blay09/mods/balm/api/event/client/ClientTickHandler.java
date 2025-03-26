package net.blay09.mods.balm.api.event.client;

import net.minecraft.client.Minecraft;

@FunctionalInterface
public interface ClientTickHandler {
    void handle(Minecraft client);
}
