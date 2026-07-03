package net.blay09.mods.balm.neoforge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.commands.BalmClientCommands;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.BalmClientTooltipComponentRegistrar;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.color.item.BalmItemColorRegistrar;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.model.item.BalmItemPropertyRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.client.CommonBalmClientRuntime;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.balm.neoforge.client.color.block.internal.NeoForgeBalmBlockColorRegistrar;
import net.blay09.mods.balm.neoforge.client.color.item.internal.NeoForgeBalmItemColorRegistrar;
import net.blay09.mods.balm.neoforge.client.gui.screens.inventory.internal.NeoForgeBalmMenuScreenRegistrar;
import net.blay09.mods.balm.neoforge.client.internal.NeoForgeBalmClientTooltipComponentRegistrar;
import net.blay09.mods.balm.neoforge.client.internal.NeoForgeBalmKeyMappingRegistrar;
import net.blay09.mods.balm.neoforge.client.internal.commands.NeoForgeBalmClientCommands;
import net.blay09.mods.balm.neoforge.client.keymappings.NeoForgeBalmKeyMappings;
import net.blay09.mods.balm.neoforge.client.model.geom.internal.NeoForgeBalmModelLayerRegistrar;
import net.blay09.mods.balm.neoforge.client.model.item.internal.NeoForgeBalmItemPropertyRegistrar;
import net.blay09.mods.balm.neoforge.client.particle.internal.NeoForgeBalmParticleProviderRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.block.model.internal.NeoForgeBalmBlockStateModelRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.blockentity.internal.NeoForgeBalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.chunk.internal.NeoForgeBalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.entity.internal.NeoForgeBalmEntityRendererRegistrar;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmModels;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmRenderers;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmTextures;
import net.blay09.mods.balm.neoforge.client.screen.NeoForgeBalmScreens;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmClientEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.blay09.mods.balm.server.packs.resources.internal.NeoForgeBalmClientResourceReloadListenerRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;

import java.util.function.Consumer;

public class NeoForgeBalmClientRuntime extends CommonBalmClientRuntime<NeoForgeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmRenderers renderers = new NeoForgeBalmRenderers(legacyNamespaceResolver);
    @Deprecated
    private final BalmTextures textures = new NeoForgeBalmTextures();
    @Deprecated
    private final BalmScreens screens = new NeoForgeBalmScreens(legacyNamespaceResolver);
    @Deprecated
    private final BalmKeyMappings keyMappings = new NeoForgeBalmKeyMappings(legacyNamespaceResolver);
    private final BalmModels models = new NeoForgeBalmModels(legacyNamespaceResolver);
    private final BalmClientCommands clientCommands = new NeoForgeBalmClientCommands();

    public NeoForgeBalmClientRuntime() {
        NeoForgeBalmClientEvents.registerEvents(((NeoForgeBalmEvents) Balm.getEvents()));
    }

    @Override
    public BalmRenderers getRenderers() {
        return renderers;
    }

    @Override
    @Deprecated
    public BalmTextures getTextures() {
        return textures;
    }

    @Override
    @Deprecated
    public BalmScreens getScreens() {
        return screens;
    }

    @Override
    public BalmModels getModels() {
        return models;
    }

    @Override
    public BalmClientCommands clientCommands() {
        return clientCommands;
    }

    @Override
    @Deprecated
    public BalmKeyMappings getKeyMappings() {
        return keyMappings;
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
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager reloadableResourceManager) {
            reloadableResourceManager.registerReloadListener(reloadListener);
        }
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
                modBus.addListener((ModelEvent.RegisterAdditional event) -> initializer.accept(new NeoForgeBalmBlockStateModelRegistrar(event)));
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
    public void itemColors(String namespace, Consumer<BalmItemColorRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((RegisterColorHandlersEvent.Item event) -> initializer.accept(new NeoForgeBalmItemColorRegistrar(event)));
            }
        });
    }

    @Override
    public void itemProperties(String namespace, Consumer<BalmItemPropertyRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((FMLClientSetupEvent event) -> event.enqueueWork(() -> initializer.accept(NeoForgeBalmItemPropertyRegistrar.INSTANCE)));
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
                modBus.addListener((RegisterClientReloadListenersEvent event) -> initializer.accept(new NeoForgeBalmClientResourceReloadListenerRegistrar(event)));
            }
        });
    }

    @Override
    public void clientTooltipComponents(String namespace, Consumer<BalmClientTooltipComponentRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(IEventBus modBus)) {
                modBus.addListener((RegisterClientTooltipComponentFactoriesEvent event) -> initializer.accept(new NeoForgeBalmClientTooltipComponentRegistrar(event)));
            }
        });
    }

}
