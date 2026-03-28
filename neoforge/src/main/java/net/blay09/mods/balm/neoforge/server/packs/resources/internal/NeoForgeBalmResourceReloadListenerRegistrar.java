package net.blay09.mods.balm.neoforge.server.packs.resources.internal;

import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import java.util.function.Function;
import java.util.function.Consumer;

public class NeoForgeBalmResourceReloadListenerRegistrar implements BalmResourceReloadListenerRegistrar {

    private final String namespace;
    private final AddServerReloadListenersEvent event;

    public NeoForgeBalmResourceReloadListenerRegistrar(String namespace, AddServerReloadListenersEvent event) {
        this.namespace = namespace;
        this.event = event;
    }

    @Override
    public void register(String name, Function<HolderLookup.Provider, PreparableReloadListener> listenerFactory) {
        event.addListener(Identifier.fromNamespaceAndPath(namespace, name), listenerFactory.apply(event.getServerResources().getRegistryLookup()));
    }

    @Override
    public void register(String name, Consumer<ResourceManager> reloadListener) {
        event.addListener(Identifier.fromNamespaceAndPath(namespace, name), (ResourceManagerReloadListener) reloadListener::accept);
    }
}
