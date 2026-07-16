package net.blay09.mods.balm.fabric.server.packs.resources.internal;

import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.fabricmc.fabric.api.resource.v1.DataResourceLoader;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.util.function.Consumer;
import java.util.function.Function;

public class FabricBalmResourceReloadListenerRegistrar implements BalmResourceReloadListenerRegistrar {
    private static final VanillaKeys VANILLA_KEYS = new VanillaKeys() {
        @Override
        public Identifier functions() {
            return ResourceReloaderKeys.Server.FUNCTIONS;
        }
    };

    private final String namespace;

    public FabricBalmResourceReloadListenerRegistrar(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void register(String name, Function<HolderLookup.Provider, PreparableReloadListener> listenerFactory) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        DataResourceLoader.get().registerReloadListener(identifier, listenerFactory);
    }

    @Override
    public void register(String name, Consumer<ResourceManager> reloadListener) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(identifier, (ResourceManagerReloadListener) reloadListener::accept);
    }

    @Override
    public void addDependency(Identifier first, Identifier second) {
        ResourceLoader.get(PackType.SERVER_DATA).addListenerOrdering(first, second);
    }

    @Override
    public VanillaKeys vanillaKeys() {
        return VANILLA_KEYS;
    }
}
