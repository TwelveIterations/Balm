package net.blay09.mods.balm.neoforge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.client.CommonBalmClientRuntime;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.balm.neoforge.client.color.block.NeoForgeBalmBlockColorRegistrar;
import net.blay09.mods.balm.neoforge.client.event.NeoForgeBalmClientEventMappings;
import net.blay09.mods.balm.neoforge.client.gui.screens.inventory.NeoForgeBalmMenuScreenRegistrar;
import net.blay09.mods.balm.neoforge.client.model.geom.NeoForgeBalmModelLayerRegistrar;
import net.blay09.mods.balm.neoforge.client.particle.NeoForgeBalmParticleProviderRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.chunk.NeoForgeBalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.block.model.NeoForgeBalmBlockStateModelRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.blockentity.NeoForgeBalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.entity.NeoForgeBalmEntityRendererRegistrar;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmModels;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmRenderers;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmClientEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.blay09.mods.balm.neoforge.server.packs.resources.NeoForgeBalmClientResourceReloadListenerRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;

import java.util.function.Consumer;

public class NeoForgeBalmClientRuntime extends CommonBalmClientRuntime<NeoForgeLoadContext> {

    @Deprecated
    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    @Deprecated
    private final BalmRenderers renderers = new NeoForgeBalmRenderers(legacyNamespaceResolver);
    @Deprecated
    private final BalmModels models = new NeoForgeBalmModels(legacyNamespaceResolver);

    public NeoForgeBalmClientRuntime() {
        NeoForgeBalmClientEvents.registerEvents(((NeoForgeBalmEvents) Balm.events()));
        NeoForgeBalmClientEventMappings.bind();
    }

    @Override
    @Deprecated
    public BalmRenderers getRenderers() {
        return renderers;
    }

    @Override
    @Deprecated
    public BalmModels getModels() {
        return models;
    }

    @Override
    public void initializeMod(String modId, NeoForgeLoadContext context, Consumer<BalmClientRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmClientRegistrars(this, modId));

        final var modEventBus = context.modBus();
        ModBusEventRegisters.register(modId, modEventBus);
    }

    @Override
    public void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        resourceReloadListeners(identifier.getNamespace(), registrar -> registrar.register(identifier.getPath(), reloadListener));
    }

    @Override
    public void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> initializer.accept(new NeoForgeBalmBlockEntityRendererRegistrar(event)));
            }
        });
    }

    @Override
    public void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> initializer.accept(new NeoForgeBalmEntityRendererRegistrar(event)));
            }
        });
    }

    @Override
    public void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((RegisterMenuScreensEvent event) -> initializer.accept(new NeoForgeBalmMenuScreenRegistrar(event)));
            }
        });
    }

    @Override
    public void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((ModelEvent.RegisterStandalone event) -> initializer.accept(new NeoForgeBalmBlockStateModelRegistrar(event)));
            }
        });
    }

    @Override
    public void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((RegisterKeyMappingsEvent event) -> initializer.accept(new NeoForgeBalmKeyMappingRegistrar(event)));
            }
        });
    }

    @Override
    public void modelLayers(String namespace, Consumer<BalmModelLayerRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) -> initializer.accept(new NeoForgeBalmModelLayerRegistrar(event)));
            }
        });
    }

    @Override
    public void blockColors(String namespace, Consumer<BalmBlockColorRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((RegisterColorHandlersEvent.Block event) -> initializer.accept(new NeoForgeBalmBlockColorRegistrar(event)));
            }
        });
    }

    @Override
    public void particleProviders(String namespace, Consumer<BalmParticleProviderRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((RegisterParticleProvidersEvent event) -> initializer.accept(new NeoForgeBalmParticleProviderRegistrar(event)));
            }
        });
    }

    @Override
    public void blockRenderTypes(String namespace, Consumer<BalmBlockRenderTypeRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(() -> initializer.accept(new NeoForgeBalmBlockRenderTypeRegistrar())));
            }
        });
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmClientResourceReloadListenerRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((AddClientReloadListenersEvent event) -> initializer.accept(new NeoForgeBalmClientResourceReloadListenerRegistrar(namespace, event)));
            }
        });
    }

}
