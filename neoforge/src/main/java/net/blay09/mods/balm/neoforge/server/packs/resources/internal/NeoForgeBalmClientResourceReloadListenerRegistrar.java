package net.blay09.mods.balm.neoforge.server.packs.resources.internal;

import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.client.resources.VanillaClientListeners;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

public class NeoForgeBalmClientResourceReloadListenerRegistrar implements BalmClientResourceReloadListenerRegistrar {
    private static final VanillaKeys VANILLA_KEYS = new VanillaKeys() {
        @Override
        public Identifier blockEntityRenderDispatcher() {
            return VanillaClientListeners.BLOCK_ENTITY_RENDERER;
        }

        @Override
        public Identifier cloudRenderer() {
            return VanillaClientListeners.CLOUD_RENDERER;
        }

        @Override
        public Identifier equipmentAssets() {
            return VanillaClientListeners.EQUIPMENT_ASSETS;
        }

        @Override
        public Identifier entityRenderDispatcher() {
            return VanillaClientListeners.ENTITY_RENDERER;
        }

        @Override
        public Identifier dryFoliageColor() {
            return VanillaClientListeners.DRY_FOLIAGE_COLOR;
        }

        @Override
        public Identifier foliageColor() {
            return VanillaClientListeners.FOLIAGE_COLOR;
        }

        @Override
        public Identifier fonts() {
            return VanillaClientListeners.FONTS;
        }

        @Override
        public Identifier grassColor() {
            return VanillaClientListeners.GRASS_COLOR;
        }

        @Override
        public Identifier atlas() {
            return VanillaClientListeners.ATLASES;
        }

        @Override
        public Identifier languages() {
            return VanillaClientListeners.LANGUAGE;
        }

        @Override
        public Identifier models() {
            return VanillaClientListeners.MODELS;
        }

        @Override
        public Identifier particles() {
            return VanillaClientListeners.PARTICLE_RESOURCES;
        }

        @Override
        public Identifier shaders() {
            return VanillaClientListeners.SHADERS;
        }

        @Override
        public Identifier sounds() {
            return VanillaClientListeners.SOUNDS;
        }

        @Override
        public Identifier splashTexts() {
            return VanillaClientListeners.SPLASHES;
        }

        @Override
        public Identifier textures() {
            return VanillaClientListeners.TEXTURES;
        }

        @Override
        public Identifier waypointStyle() {
            return VanillaClientListeners.WAYPOINT_STYLES;
        }
    };

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

    @Override
    public void addDependency(Identifier first, Identifier second) {
        event.addDependency(first, second);
    }

    @Override
    public VanillaKeys vanillaKeys() {
        return VANILLA_KEYS;
    }
}
