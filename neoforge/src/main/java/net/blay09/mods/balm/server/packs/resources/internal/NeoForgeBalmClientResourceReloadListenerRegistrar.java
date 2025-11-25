package net.blay09.mods.balm.server.packs.resources.internal;

import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

public class NeoForgeBalmClientResourceReloadListenerRegistrar implements BalmClientResourceReloadListenerRegistrar {

    private final RegisterClientReloadListenersEvent event;

    public NeoForgeBalmClientResourceReloadListenerRegistrar(RegisterClientReloadListenersEvent event) {
        this.event = event;
    }

    @Override
    public void register(String name, PreparableReloadListener listener) {
        event.registerReloadListener(listener);
    }
}
