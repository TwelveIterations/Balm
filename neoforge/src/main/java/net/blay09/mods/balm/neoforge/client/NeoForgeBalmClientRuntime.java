package net.blay09.mods.balm.neoforge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.client.CommonBalmClientRuntime;
import net.blay09.mods.balm.neoforge.ModBusEventRegisters;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.balm.neoforge.client.keymappings.NeoForgeBalmKeyMappings;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmModels;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmRenderers;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmTextures;
import net.blay09.mods.balm.neoforge.client.screen.NeoForgeBalmScreens;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmClientEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.neoforged.fml.ModLoadingContext;

public class NeoForgeBalmClientRuntime extends CommonBalmClientRuntime<NeoForgeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmRenderers renderers = new NeoForgeBalmRenderers(legacyNamespaceResolver);
    @Deprecated(forRemoval = true, since = "1.21.5")
    private final BalmTextures textures = new NeoForgeBalmTextures();
    private final BalmScreens screens = new NeoForgeBalmScreens(legacyNamespaceResolver);
    private final BalmKeyMappings keyMappings = new NeoForgeBalmKeyMappings(legacyNamespaceResolver);
    private final BalmModels models = new NeoForgeBalmModels(legacyNamespaceResolver);

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    public NeoForgeBalmClientRuntime() {
        NeoForgeBalmClientEvents.registerEvents(((NeoForgeBalmEvents) Balm.getEvents()));
    }

    @Override
    public BalmRenderers getRenderers() {
        return renderers;
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.21.5")
    public BalmTextures getTextures() {
        return textures;
    }

    @Override
    public BalmScreens getScreens() {
        return screens;
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
        getRegistrations(identifier.getNamespace()).reloadListeners.add(new ReloadListenerRegistration(identifier, reloadListener));
    }

    private Registrations getRegistrations(String modId) {
        return registrations.computeIfAbsent(modId, it -> new Registrations());
    }

    record ReloadListenerRegistration(ResourceLocation identifier, PreparableReloadListener listener) {
    }

    private static class Registrations {
        public final List<ReloadListenerRegistration> reloadListeners = new ArrayList<>();

        @SubscribeEvent
        public void addClientReloadListeners(AddClientReloadListenersEvent event) {
            for (final var reloadListener : reloadListeners) {
                event.addListener(reloadListener.identifier(), reloadListener.listener());
            }
        }
    }
}
