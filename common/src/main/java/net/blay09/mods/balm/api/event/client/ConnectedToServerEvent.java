package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.Minecraft;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ClientLifecycleCallback#CONNECTED_TO_SERVER} instead.
 */
@Deprecated
public class ConnectedToServerEvent extends BalmEvent {
    private final Minecraft client;

    public ConnectedToServerEvent(Minecraft client) {
        this.client = client;
    }

    public Minecraft getClient() {
        return client;
    }
}
