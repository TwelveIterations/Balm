package net.blay09.mods.balm.api.client.module;

import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.client.screen.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
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
