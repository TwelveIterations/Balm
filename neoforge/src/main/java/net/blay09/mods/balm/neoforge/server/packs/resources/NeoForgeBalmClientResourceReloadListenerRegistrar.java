package net.blay09.mods.balm.neoforge.server.packs.resources;

import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

public class NeoForgeBalmClientResourceReloadListenerRegistrar implements BalmClientResourceReloadListenerRegistrar {

    private final String namespace;
    private final AddClientReloadListenersEvent event;

    public NeoForgeBalmClientResourceReloadListenerRegistrar(String namespace, AddClientReloadListenersEvent event) {
        this.namespace = namespace;
        this.event = event;
    }

    @Override
    public void register(String name, PreparableReloadListener listener) {
        event.addListener(Identifier.fromNamespaceAndPath(namespace, name), listener);
    }
}
