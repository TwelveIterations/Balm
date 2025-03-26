package net.blay09.mods.balm.api.event.client;

import net.minecraft.client.multiplayer.ClientLevel;

@FunctionalInterface
public interface ClientLevelTickHandler {
    void handle(ClientLevel client);
}
