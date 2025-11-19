package net.blay09.mods.balm.fabric.client.internal.platform.runtime.internal;

import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.fabric.client.internal.FabricBalmKeyMappingRegistrar;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.blay09.mods.balm.platform.runtime.internal.BalmLoadContexts;
import net.blay09.mods.balm.client.platform.runtime.internal.CommonBalmClientRuntime;
import net.blay09.mods.balm.fabric.client.internal.event.FabricBalmClientEventMappings;
import net.blay09.mods.balm.fabric.client.internal.event.FabricBalmSupplementalClientEvents;
import net.blay09.mods.balm.fabric.client.internal.gui.screens.inventory.FabricBalmMenuScreenRegistrar;
import net.blay09.mods.balm.fabric.client.internal.renderer.blockentity.FabricBalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.fabric.client.internal.renderer.entity.FabricBalmEntityRendererRegistrar;
import net.blay09.mods.balm.fabric.client.internal.model.geom.FabricBalmModelLayerRegistrar;
import net.blay09.mods.balm.fabric.client.internal.color.block.FabricBalmBlockColorRegistrar;
import net.blay09.mods.balm.fabric.client.internal.particle.FabricBalmParticleProviderRegistrar;
import net.blay09.mods.balm.fabric.client.internal.renderer.chunk.FabricBalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.fabric.server.packs.resources.internal.FabricBalmClientResourceReloadListenerRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;

import java.util.function.Consumer;

public class FabricBalmClientRuntime extends CommonBalmClientRuntime<FabricLoadContext> {

    public FabricBalmClientRuntime() {
        FabricBalmSupplementalClientEvents.initialize();
        FabricBalmClientEventMappings.bind();
    }

    @Override
    public void initializeMod(String modId, FabricLoadContext context, Consumer<BalmClientRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmClientRegistrars(this, modId));
    }

    @Override
    public void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer) {
        initializer.accept(FabricBalmBlockEntityRendererRegistrar.INSTANCE);
    }

    @Override
    public void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer) {
        initializer.accept(FabricBalmEntityRendererRegistrar.INSTANCE);
    }

    @Override
    public void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer) {
        // TODO ModelLoadingPlugin.register(context -> {
        // TODO     initializer.accept(new FabricBalmBlockStateModelRegistrar(context));
        // TODO });
    }

    @Override
    public void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer) {
        initializer.accept(FabricBalmMenuScreenRegistrar.INSTANCE);
    }

    @Override
    public void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer) {
        initializer.accept(FabricBalmKeyMappingRegistrar.INSTANCE);
    }

    @Override
    public void modelLayers(String namespace, Consumer<BalmModelLayerRegistrar> initializer) {
        initializer.accept(FabricBalmModelLayerRegistrar.INSTANCE);
    }

    @Override
    public void blockColors(String namespace, Consumer<BalmBlockColorRegistrar> initializer) {
        initializer.accept(FabricBalmBlockColorRegistrar.INSTANCE);
    }

    @Override
    public void particleProviders(String namespace, Consumer<BalmParticleProviderRegistrar> initializer) {
        initializer.accept(FabricBalmParticleProviderRegistrar.INSTANCE);
    }

    @Override
    public void blockRenderTypes(String namespace, Consumer<BalmBlockRenderTypeRegistrar> initializer) {
        initializer.accept(FabricBalmBlockRenderTypeRegistrar.INSTANCE);
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmClientResourceReloadListenerRegistrar> initializer) {
        initializer.accept(new FabricBalmClientResourceReloadListenerRegistrar(namespace));
    }
}