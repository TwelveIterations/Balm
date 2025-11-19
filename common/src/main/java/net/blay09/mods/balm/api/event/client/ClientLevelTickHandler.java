package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.client.event.callback.ClientTickCallback;
import net.minecraft.client.multiplayer.ClientLevel;

/**
 * @deprecated Use {@link ClientTickCallback.ClientLevelTick} instead.
 */
@Deprecated
@FunctionalInterface
public interface ClientLevelTickHandler {
    void handle(ClientLevel client);
}
