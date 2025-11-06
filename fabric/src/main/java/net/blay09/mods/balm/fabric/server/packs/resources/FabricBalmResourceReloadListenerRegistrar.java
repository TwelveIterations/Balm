package net.blay09.mods.balm.fabric.server.packs.resources;

import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public class FabricBalmResourceReloadListenerRegistrar implements BalmResourceReloadListenerRegistrar {

    private final String namespace;

    public FabricBalmResourceReloadListenerRegistrar(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void register(String name, Function<HolderLookup.Provider, PreparableReloadListener> listenerFactory) {
        final var identifier = ResourceLocation.fromNamespaceAndPath(namespace, name);
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(identifier, providers -> new IdentifiableResourceReloadListener() {
            private final PreparableReloadListener listener = listenerFactory.apply(providers);

            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }

            @Override
            public CompletableFuture<Void> reload(SharedState sharedState, Executor executor, PreparationBarrier preparationBarrier, Executor executor2) {
                return listener.reload(sharedState, executor, preparationBarrier, executor2);
            }
        });
    }

    @Override
    public void register(String name, Consumer<ResourceManager> reloadListener) {
        final var identifier = ResourceLocation.fromNamespaceAndPath(namespace, name);
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                reloadListener.accept(resourceManager);
            }

            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }
        });
    }
}
