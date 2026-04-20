package net.blay09.mods.balm.client.platform.runtime.internal;

import net.blay09.mods.balm.client.platform.module.BalmClientModule;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.BalmClientTooltipComponentRegistrar;
import net.blay09.mods.balm.client.BalmRangeSelectItemModelPropertyRegistrar;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.platform.BalmClientHooks;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;

import java.util.function.Consumer;

public interface BalmClientRuntime<TLoadContext extends BalmRuntimeLoadContext> {
    void initializeMod(String modId, TLoadContext context, Consumer<BalmClientRegistrars> initializer);

    default void initializeModule(BalmClientModule module) {
        final var modId = module.getId().getNamespace();
        resourceReloadListeners(modId, module::registerClientReloadListeners);
        blockColors(modId, module::registerBlockColors);
        blockEntityRenderers(modId, module::registerBlockEntityRenderers);
        entityRenderers(modId, module::registerEntityRenderers);
        particleProviders(modId, module::registerParticleProviders);
        modelLayers(modId, module::registerModelLayers);
        menuScreens(modId, module::registerMenuScreens);
        blockStateModels(modId, module::registerBlockStateModels);
        keyMappings(modId, module::registerKeyMappings);
        clientTooltipComponents(modId, module::registerClientTooltipComponents);
        rangeSelectItemModelProperties(modId, module::registerRangeSelectItemModelProperties);

        module.initialize();
    }

    boolean isReady();

    void onRuntimeAvailable(Runnable callback);

    BalmClientHooks clientHooks();

    void registerModule(BalmClientRegistrars registrars, BalmClientModule module);

    void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer);

    void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer);

    void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer);

    void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer);

    void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer);

    void modelLayers(String namespace, Consumer<BalmModelLayerRegistrar> initializer);

    void blockColors(String namespace, Consumer<BalmBlockColorRegistrar> initializer);

    void particleProviders(String namespace, Consumer<BalmParticleProviderRegistrar> initializer);

    void resourceReloadListeners(String namespace, Consumer<BalmClientResourceReloadListenerRegistrar> initializer);

    void clientTooltipComponents(String namespace, Consumer<BalmClientTooltipComponentRegistrar> initializer);

    void rangeSelectItemModelProperties(String namespace, Consumer<BalmRangeSelectItemModelPropertyRegistrar> initializer);
}
