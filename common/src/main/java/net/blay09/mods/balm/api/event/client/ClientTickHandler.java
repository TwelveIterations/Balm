package net.blay09.mods.balm.api.event.client;

import net.minecraft.client.Minecraft;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ClientTickCallback} instead.
 */
@Deprecated
@FunctionalInterface
public interface ClientTickHandler {
    void handle(Minecraft client);
}
