package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.Minecraft;

public class DisconnectedFromServerEvent extends BalmEvent {
    private final Minecraft client;

    public DisconnectedFromServerEvent(Minecraft client) {
        this.client = client;
    }

    public Minecraft getClient() {
        return client;
    }
}
