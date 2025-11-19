package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.client.CommonBalmClientRuntime;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.blay09.mods.balm.forge.client.color.block.ForgeBalmBlockColorRegistrar;
import net.blay09.mods.balm.forge.client.event.ForgeBalmClientEventMappings;
import net.blay09.mods.balm.forge.client.gui.screens.inventory.ForgeBalmMenuScreenRegistrar;
import net.blay09.mods.balm.forge.client.model.geom.ForgeBalmModelLayerRegistrar;
import net.blay09.mods.balm.forge.client.particle.ForgeBalmParticleProviderRegistrar;
import net.blay09.mods.balm.forge.client.renderer.block.model.ForgeBalmBlockStateModelRegistrar;
import net.blay09.mods.balm.forge.client.renderer.blockentity.ForgeBalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.forge.client.renderer.chunk.ForgeBalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.forge.client.renderer.entity.ForgeBalmEntityRendererRegistrar;
import net.blay09.mods.balm.forge.event.ForgeBalmClientEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEventMappings;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;
import net.blay09.mods.balm.forge.server.packs.resources.ForgeBalmClientResourceReloadListenerRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Consumer;

public class ForgeBalmClientRuntime extends CommonBalmClientRuntime<ForgeLoadContext> {

    public ForgeBalmClientRuntime() {
        ForgeBalmClientEvents.registerEvents(((ForgeBalmEvents) Balm.getEvents()));
        ForgeBalmClientEventMappings.bind();
    }

    @Override
    public void initializeMod(String modId, ForgeLoadContext context, Consumer<BalmClientRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmClientRegistrars(this, modId));

        ModBusEventRegisters.register(modId, context.modBusGroup());
    }

    @Override
    public void addResourceReloadListener(Identifier identifier, PreparableReloadListener reloadListener) {
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager reloadableResourceManager) {
            reloadableResourceManager.registerReloadListener(reloadListener);
        }
    }

    @Override
    public void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer) {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener((event) -> initializer.accept(new ForgeBalmBlockEntityRendererRegistrar(event)));
    }

    @Override
    public void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer) {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener((event) -> initializer.accept(new ForgeBalmEntityRendererRegistrar(event)));
    }

    @Override
    public void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer) {
        ModelEvent.RegisterModelStateDefinitions.BUS.addListener(event -> initializer.accept(new ForgeBalmBlockStateModelRegistrar(event)));
    }

    @Override
    public void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer) {
        final var busGroup = ModBusEventRegisters.getBusGroup(namespace);
        FMLClientSetupEvent.getBus(busGroup)
                .addListener((event)
                        -> event.enqueueWork(() ->
                        initializer.accept(ForgeBalmMenuScreenRegistrar.INSTANCE)));
    }

    @Override
    public void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer) {
        RegisterKeyMappingsEvent.BUS.addListener(event -> initializer.accept(new ForgeBalmKeyMappingRegistrar(event)));
    }

    @Override
    public void modelLayers(String namespace, Consumer<BalmModelLayerRegistrar> initializer) {
        EntityRenderersEvent.RegisterLayerDefinitions.BUS.addListener(event -> initializer.accept(new ForgeBalmModelLayerRegistrar(event)));
    }

    @Override
    public void blockColors(String namespace, Consumer<BalmBlockColorRegistrar> initializer) {
        RegisterColorHandlersEvent.Block.BUS.addListener(event -> initializer.accept(new ForgeBalmBlockColorRegistrar(event)));
    }

    @Override
    public void particleProviders(String namespace, Consumer<BalmParticleProviderRegistrar> initializer) {
        RegisterParticleProvidersEvent.BUS.addListener(event -> initializer.accept(new ForgeBalmParticleProviderRegistrar(event)));
    }

    @Override
    public void blockRenderTypes(String namespace, Consumer<BalmBlockRenderTypeRegistrar> initializer) {
        final var busGroup = ModBusEventRegisters.getBusGroup(namespace);
        FMLClientSetupEvent.getBus(busGroup)
                .addListener((event)
                        -> event.enqueueWork(() -> initializer.accept(new ForgeBalmBlockRenderTypeRegistrar())));
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmClientResourceReloadListenerRegistrar> initializer) {
        initializer.accept(ForgeBalmClientResourceReloadListenerRegistrar.INSTANCE);
    }
}
