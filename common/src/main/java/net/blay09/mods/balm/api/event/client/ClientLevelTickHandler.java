package net.blay09.mods.balm.api.event.client;

import net.minecraft.client.multiplayer.ClientLevel;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ClientTickCallback.Level} instead.
 */
@Deprecated
@FunctionalInterface
public interface ClientLevelTickHandler {
    void handle(ClientLevel client);
}
