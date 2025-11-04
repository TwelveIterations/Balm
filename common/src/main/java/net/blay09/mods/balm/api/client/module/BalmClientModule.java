package net.blay09.mods.balm.api.client.module;

import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.minecraft.resources.ResourceLocation;

public interface BalmClientModule {
    ResourceLocation getId();

    default void registerEvents(BalmEvents events) {
    }

    default void registerModels(BalmModels models) {
    }

    default void registerRenderers(BalmRenderers renderers) {
    }

    default void registerBlockEntityRenderers(BalmBlockEntityRendererRegistrar blockEntityRenderers) {
    }

    default void registerEntityRenderers(BalmEntityRendererRegistrar entityRenderers) {
    }

    default void registerMenuScreens(BalmMenuScreenRegistrar menuScreens) {
    }

    /**
     * @deprecated Use {@link #registerMenuScreens(BalmMenuScreenRegistrar)} instead.
     */
    @Deprecated
    default void registerScreens(BalmScreens screens) {
    }

    default void registerKeyMappings(BalmKeyMappings keyMappings) {
    }

    default void initialize() {
    }
}
