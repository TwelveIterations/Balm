package net.blay09.mods.balm.fabric.server.packs.resources.internal;

import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class FabricBalmClientResourceReloadListenerRegistrar implements BalmClientResourceReloadListenerRegistrar {

    private final String namespace;

    public FabricBalmClientResourceReloadListenerRegistrar(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void register(String name, PreparableReloadListener listener) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(identifier, listener);
    }
}
