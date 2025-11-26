package net.blay09.mods.balm.api.client.module;

import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.color.item.BalmItemColorRegistrar;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("DeprecatedIsStillUsed")
public interface BalmClientModule {
    ResourceLocation getId();

    default void registerEvents(BalmEvents events) {
    }

    default void registerModels(BalmModels models) {
    }

    default void registerRenderers(BalmRenderers renderers) {
    }

    /**
     * @deprecated Use {@link #registerMenuScreens(BalmMenuScreenRegistrar)} instead.
     */
    @Deprecated
    default void registerScreens(BalmScreens screens) {
    }

    /**
     * @deprecated Use {@link #registerKeyMappings(BalmKeyMappingRegistrar)} instead.
     */
    @Deprecated
    default void registerKeyMappings(BalmKeyMappings keyMappings) {
    }

    default void initialize() {
    }

    default void registerBlockStateModels(BalmBlockStateModelRegistrar models) {
    }

    default void registerModelLayers(BalmModelLayerRegistrar modelLayers) {
    }

    default void registerBlockColors(BalmBlockColorRegistrar blockColors) {
    }

    default void registerItemColors(BalmItemColorRegistrar blockColors) {
    }

    default void registerParticleProviders(BalmParticleProviderRegistrar particles) {
    }

    default void registerBlockRenderTypes(BalmBlockRenderTypeRegistrar blockRenderTypes) {
    }

    default void registerBlockEntityRenderers(BalmBlockEntityRendererRegistrar blockEntityRenderers) {
    }

    default void registerEntityRenderers(BalmEntityRendererRegistrar entityRenderers) {
    }

    default void registerMenuScreens(BalmMenuScreenRegistrar menuScreens) {
    }

    default void registerKeyMappings(BalmKeyMappingRegistrar keyMappings) {
    }

    default void registerClientReloadListeners(BalmClientResourceReloadListenerRegistrar resourceReloadListeners) {
    }
}
