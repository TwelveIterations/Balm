package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
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
import net.blay09.mods.balm.forge.client.color.item.ForgeBalmItemColorRegistrar;
import net.blay09.mods.balm.forge.client.gui.screens.inventory.ForgeBalmMenuScreenRegistrar;
import net.blay09.mods.balm.forge.client.internal.ForgeBalmClientTooltipComponentRegistrar;
import net.blay09.mods.balm.forge.client.keymappings.ForgeBalmKeyMappings;
import net.blay09.mods.balm.forge.client.model.geom.ForgeBalmModelLayerRegistrar;
import net.blay09.mods.balm.forge.client.particle.ForgeBalmParticleProviderRegistrar;
import net.blay09.mods.balm.forge.client.renderer.block.model.ForgeBalmBlockStateModelRegistrar;
import net.blay09.mods.balm.forge.client.renderer.blockentity.ForgeBalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.forge.client.renderer.chunk.ForgeBalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.forge.client.renderer.entity.ForgeBalmEntityRendererRegistrar;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmModels;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmRenderers;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmTextures;
import net.blay09.mods.balm.forge.client.screen.ForgeBalmScreens;
import net.blay09.mods.balm.forge.event.ForgeBalmClientEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;
import net.blay09.mods.balm.forge.server.packs.resources.ForgeBalmClientResourceReloadListenerRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;

@SuppressWarnings("removal")
public class ForgeBalmClientRuntime extends CommonBalmClientRuntime<BalmRuntimeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmRenderers renderers = new ForgeBalmRenderers(legacyNamespaceResolver);
    @Deprecated
    private final BalmScreens screens = new ForgeBalmScreens(legacyNamespaceResolver);
    @Deprecated
    private final BalmKeyMappings keyMappings = new ForgeBalmKeyMappings(legacyNamespaceResolver);
    private final BalmModels models = new ForgeBalmModels(legacyNamespaceResolver);

    @Deprecated
    private final BalmTextures textures = new ForgeBalmTextures();

    public ForgeBalmClientRuntime() {
        ForgeBalmClientEvents.registerEvents(((ForgeBalmEvents) Balm.getEvents()));
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
    @Deprecated
    public BalmKeyMappings getKeyMappings() {
        return keyMappings;
    }

    @SuppressWarnings("removal")
    @Override
    public void initializeMod(String modId, BalmRuntimeLoadContext context, Consumer<BalmClientRegistrars> initializer) {
        ForgeLoadContext forgeLoadContext;
        if (context instanceof ForgeLoadContext) {
            forgeLoadContext = (ForgeLoadContext) context;
        } else {
            forgeLoadContext = new ForgeLoadContext(FMLJavaModLoadingContext.get().getModEventBus());
        }
        BalmLoadContexts.register(modId, forgeLoadContext);

        initializer.accept(new BalmClientRegistrars(this, modId));

        final var modEventBus = forgeLoadContext.modEventBus();
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
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((EntityRenderersEvent.RegisterRenderers event)
                -> initializer.accept(new ForgeBalmBlockEntityRendererRegistrar(event))));
    }

    @Override
    public void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer) {
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((EntityRenderersEvent.RegisterRenderers event)
                -> initializer.accept(new ForgeBalmEntityRendererRegistrar(event))));
    }

    @Override
    public void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer) {
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((ModelEvent.RegisterAdditional event)
                -> initializer.accept(new ForgeBalmBlockStateModelRegistrar(event))));
    }

    @Override
    public void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer) {
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((FMLClientSetupEvent event)
                -> event.enqueueWork(()
                -> initializer.accept(ForgeBalmMenuScreenRegistrar.INSTANCE))));
    }

    @Override
    public void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer) {
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((RegisterKeyMappingsEvent event)
                -> initializer.accept(new ForgeBalmKeyMappingRegistrar(event))));
    }

    @Override
    public void modelLayers(String namespace, Consumer<BalmModelLayerRegistrar> initializer) {
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event)
                -> initializer.accept(new ForgeBalmModelLayerRegistrar(event))));
    }

    @Override
    public void blockColors(String namespace, Consumer<BalmBlockColorRegistrar> initializer) {
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((RegisterColorHandlersEvent.Block event)
                -> initializer.accept(new ForgeBalmBlockColorRegistrar(event))));
    }

    @Override
    public void itemColors(String namespace, Consumer<BalmItemColorRegistrar> initializer) {
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((RegisterColorHandlersEvent.Item event)
                -> initializer.accept(new ForgeBalmItemColorRegistrar(event))));
    }

    @Override
    public void particleProviders(String namespace, Consumer<BalmParticleProviderRegistrar> initializer) {
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((RegisterParticleProvidersEvent event)
                -> initializer.accept(new ForgeBalmParticleProviderRegistrar(event))));
    }

    @Override
    public void blockRenderTypes(String namespace, Consumer<BalmBlockRenderTypeRegistrar> initializer) {
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((FMLClientSetupEvent event)
                -> event.enqueueWork(()
                -> initializer.accept(new ForgeBalmBlockRenderTypeRegistrar()))));
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmClientResourceReloadListenerRegistrar> initializer) {
        initializer.accept(ForgeBalmClientResourceReloadListenerRegistrar.INSTANCE);
    }

    @Override
    public void clientTooltipComponents(String namespace, Consumer<BalmClientTooltipComponentRegistrar> initializer) {
        ModBusEventRegisters.register(namespace, (bus)
                -> bus.addListener((RegisterClientTooltipComponentFactoriesEvent event)
                -> initializer.accept(new ForgeBalmClientTooltipComponentRegistrar(event))));
    }
}
