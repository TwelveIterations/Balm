package net.blay09.mods.balm.client.platform.module;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.BalmClientTooltipComponentRegistrar;
import net.blay09.mods.balm.client.BalmRangeSelectItemModelPropertyRegistrar;
import net.blay09.mods.balm.client.commands.BalmClientCommands;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.minecraft.resources.Identifier;

/**
 * This interface provides an easy and structured way of interacting with Balm on the client side.
 * <p>
 * Once a module is registered using {@link BalmClient#initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)}, its <code>register{...}</code> methods will be called automatically.
 */
public interface BalmClientModule {
    /**
     * Should return a unique identifier for this module, e.g. <code>yourmod:client</code>. The namespace must be your mod id.
     * @return a unique identifier for this module.
     */
    Identifier getId();

    default void registerBlockStateModels(BalmBlockStateModelRegistrar models) {
    }

    default void registerModelLayers(BalmModelLayerRegistrar modelLayers) {
    }

    default void registerBlockColors(BalmBlockColorRegistrar blockColors) {
    }

    default void registerParticleProviders(BalmParticleProviderRegistrar particles) {
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

    default void registerClientTooltipComponents(BalmClientTooltipComponentRegistrar tooltipComponents) {
    }

    default void registerRangeSelectItemModelProperties(BalmRangeSelectItemModelPropertyRegistrar rangeSelectItemModelProperties) {
    }

    default void registerClientCommands(BalmClientCommands commands) {
    }

    default void initialize() {
    }
}
