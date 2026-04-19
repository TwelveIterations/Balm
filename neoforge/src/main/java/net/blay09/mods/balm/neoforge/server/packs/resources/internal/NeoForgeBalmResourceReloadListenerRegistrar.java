package net.blay09.mods.balm.neoforge.server.packs.resources.internal;

import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.resource.VanillaServerListeners;

import java.util.function.Function;
import java.util.function.Consumer;

public class NeoForgeBalmResourceReloadListenerRegistrar implements BalmResourceReloadListenerRegistrar {
    private static final VanillaKeys VANILLA_KEYS = new VanillaKeys() {
        @Override
        public Identifier advancements() {
            return VanillaServerListeners.ADVANCEMENTS;
        }

        @Override
        public Identifier functions() {
            return VanillaServerListeners.FUNCTIONS;
        }

        @Override
        public Identifier recipes() {
            return VanillaServerListeners.RECIPES;
        }
    };

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

    @Override
    public void addDependency(Identifier first, Identifier second) {
        event.addDependency(first, second);
    }

    @Override
    public VanillaKeys vanillaKeys() {
        return VANILLA_KEYS;
    }
}
