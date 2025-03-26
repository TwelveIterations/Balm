package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.Minecraft;

public class ClientStartedEvent extends BalmEvent {
    private final Minecraft minecraft;

    public ClientStartedEvent(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }
}
