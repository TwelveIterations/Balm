package net.blay09.mods.balm.fabric.server.packs.resources.internal;

import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class FabricBalmClientResourceReloadListenerRegistrar implements BalmClientResourceReloadListenerRegistrar {
    private static final VanillaKeys VANILLA_KEYS = new VanillaKeys() {
        @Override
        public Identifier blockEntityRenderDispatcher() {
            return ResourceReloaderKeys.Client.BLOCK_ENTITY_RENDER_DISPATCHER;
        }

        @Override
        public Identifier cloudRenderer() {
            return ResourceReloaderKeys.Client.CLOUD_RENDERER;
        }

        @Override
        public Identifier equipmentAssets() {
            return ResourceReloaderKeys.Client.EQUIPMENT_ASSETS;
        }

        @Override
        public Identifier entityRenderDispatcher() {
            return ResourceReloaderKeys.Client.ENTITY_RENDER_DISPATCHER;
        }

        @Override
        public Identifier dryFoliageColor() {
            return ResourceReloaderKeys.Client.DRY_FOLIAGE_COLOR;
        }

        @Override
        public Identifier foliageColor() {
            return ResourceReloaderKeys.Client.FOLIAGE_COLOR;
        }

        @Override
        public Identifier fonts() {
            return ResourceReloaderKeys.Client.FONTS;
        }

        @Override
        public Identifier grassColor() {
            return ResourceReloaderKeys.Client.GRASS_COLOR;
        }

        @Override
        public Identifier atlas() {
            return ResourceReloaderKeys.Client.ATLAS;
        }

        @Override
        public Identifier languages() {
            return ResourceReloaderKeys.Client.LANGUAGES;
        }

        @Override
        public Identifier models() {
            return ResourceReloaderKeys.Client.MODELS;
        }

        @Override
        public Identifier particles() {
            return ResourceReloaderKeys.Client.PARTICLES;
        }

        @Override
        public Identifier shaders() {
            return ResourceReloaderKeys.Client.SHADERS;
        }

        @Override
        public Identifier sounds() {
            return ResourceReloaderKeys.Client.SOUNDS;
        }

        @Override
        public Identifier splashTexts() {
            return ResourceReloaderKeys.Client.SPLASH_TEXTS;
        }

        @Override
        public Identifier textures() {
            return ResourceReloaderKeys.Client.TEXTURES;
        }

        @Override
        public Identifier waypointStyle() {
            return ResourceReloaderKeys.Client.WAYPOINT_STYLE;
        }
    };

    private final String namespace;

    public FabricBalmClientResourceReloadListenerRegistrar(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void register(String name, PreparableReloadListener listener) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(identifier, listener);
    }

    @Override
    public void addDependency(Identifier first, Identifier second) {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).addListenerOrdering(first, second);
    }

    @Override
    public VanillaKeys vanillaKeys() {
        return VANILLA_KEYS;
    }
}
