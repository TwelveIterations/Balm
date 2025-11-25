package net.blay09.mods.balm.forge.server.packs.resources;

import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.function.Consumer;
import java.util.function.Function;

public class ForgeBalmResourceReloadListenerRegistrar implements BalmResourceReloadListenerRegistrar {

    private final AddReloadListenerEvent event;

    public ForgeBalmResourceReloadListenerRegistrar(AddReloadListenerEvent event) {
        this.event = event;
    }

    @Override
    public void register(String name, Function<HolderLookup.Provider, PreparableReloadListener> listenerFactory) {
        event.addListener(listenerFactory.apply(event.getRegistries()));
    }

    @Override
    public void register(String name, Consumer<ResourceManager> reloadListener) {
        event.addListener((ResourceManagerReloadListener) reloadListener::accept);
    }
}
