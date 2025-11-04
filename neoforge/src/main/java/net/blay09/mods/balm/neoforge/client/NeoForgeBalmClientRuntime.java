package net.blay09.mods.balm.neoforge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.keymappings.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.client.CommonBalmClientRuntime;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.balm.neoforge.client.keymappings.NeoForgeBalmKeyMappingRegistrar;
import net.blay09.mods.balm.neoforge.client.keymappings.NeoForgeBalmKeyMappings;
import net.blay09.mods.balm.neoforge.client.renderer.blockentity.NeoForgeBalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.entity.NeoForgeBalmEntityRendererRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.block.model.NeoForgeBalmBlockStateModelRegistrar;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmModels;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmRenderers;
import net.blay09.mods.balm.neoforge.client.gui.screens.inventory.NeoForgeBalmMenuScreenRegistrar;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmClientEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NeoForgeBalmClientRuntime extends CommonBalmClientRuntime<NeoForgeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmRenderers renderers = new NeoForgeBalmRenderers(legacyNamespaceResolver);
    private final BalmKeyMappings keyMappings = new NeoForgeBalmKeyMappings(legacyNamespaceResolver);
    private final BalmModels models = new NeoForgeBalmModels(legacyNamespaceResolver);

    public NeoForgeBalmClientRuntime() {
        NeoForgeBalmClientEvents.registerEvents(((NeoForgeBalmEvents) Balm.getEvents()));
    }

    @Override
    public BalmRenderers getRenderers() {
        return renderers;
    }

    @Override
    public BalmModels getModels() {
        return models;
    }

    @Override
    public BalmKeyMappings getKeyMappings() {
        return keyMappings;
    }

    @Override
    public void initializeMod(String modId, NeoForgeLoadContext context, Runnable initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.run();

        final var modEventBus = context.modBus();
        ModBusEventRegisters.register(modId, modEventBus);
    }

    @Override
    public void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        getActiveRegistrations().reloadListeners.add(new ReloadListenerRegistration(identifier, reloadListener));
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

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(legacyNamespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    public record ReloadListenerRegistration(ResourceLocation identifier, PreparableReloadListener listener) {
    }

    public static class Registrations {
        public final List<ReloadListenerRegistration> reloadListeners = new ArrayList<>();

        @SubscribeEvent
        public void addClientReloadListeners(AddClientReloadListenersEvent event) {
            for (final var reloadListener : reloadListeners) {
                event.addListener(reloadListener.identifier(), reloadListener.listener());
            }
        }
    }
}
