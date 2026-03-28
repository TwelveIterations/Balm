package net.blay09.mods.balm.server.packs.resources.internal;

import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.function.Function;
import java.util.function.Consumer;

public class NeoForgeBalmResourceReloadListenerRegistrar implements BalmResourceReloadListenerRegistrar {

    private final AddReloadListenerEvent event;

    public NeoForgeBalmResourceReloadListenerRegistrar(AddReloadListenerEvent event) {
        this.event = event;
    }

    @Override
    public void register(String name, Function<HolderLookup.Provider, PreparableReloadListener> listenerFactory) {
        event.addListener(listenerFactory.apply(event.getServerResources().getRegistryLookup()));
    }

    @Override
    public void register(String name, Consumer<ResourceManager> reloadListener) {
        event.addListener((ResourceManagerReloadListener) reloadListener::accept);
    }
}
