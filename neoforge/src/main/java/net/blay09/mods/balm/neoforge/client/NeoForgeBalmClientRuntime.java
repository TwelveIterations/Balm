package net.blay09.mods.balm.neoforge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.balm.neoforge.client.keymappings.NeoForgeBalmKeyMappings;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmModels;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmRenderers;
import net.blay09.mods.balm.neoforge.client.screen.NeoForgeBalmScreens;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmClientEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NeoForgeBalmClientRuntime implements BalmClientRuntime<NeoForgeLoadContext> {

    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static final List<BalmClientModule> modules = Collections.synchronizedList(new ArrayList<>());
    private final BalmRenderers renderers = new NeoForgeBalmRenderers();
    private final BalmScreens screens = new NeoForgeBalmScreens();
    private final BalmKeyMappings keyMappings = new NeoForgeBalmKeyMappings();
    private final BalmModels models = new NeoForgeBalmModels();

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    private boolean ready;

    public NeoForgeBalmClientRuntime() {
        NeoForgeBalmClientEvents.registerEvents(((NeoForgeBalmEvents) Balm.getEvents()));
    }

    @Override
    public BalmRenderers getRenderers() {
        return renderers;
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
        ((NeoForgeBalmRenderers) renderers).register(modId, context.modBus());
        ((NeoForgeBalmScreens) screens).register(modId, context.modBus());
        ((NeoForgeBalmModels) models).register(modId, context.modBus());
        ((NeoForgeBalmKeyMappings) keyMappings).register(modId, context.modBus());
        ((NeoForgeBalmKeyMappings) keyMappings).register(modId, context.modBus());
        context.modBus().register(getRegistrations(modId));

        initializer.run();
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void onRuntimeAvailable(Runnable callback) {
        initCallbacks.add(callback);
        if (isReady()) {
            callback.run();
        }
    }

    @Override
    public void registerModule(BalmClientModule module) {
        modules.add(module);
        if (isReady()) {
            initializeModule(module);
        }
    }

    @Override
    public void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        getRegistrations(identifier.getNamespace()).reloadListeners.add(new ReloadListenerRegistration(identifier, reloadListener));
    }

    public void initializeRuntime() {
        ready = true;
        for (final var callback : initCallbacks) {
            callback.run();
        }
        for (final var module : modules) {
            initializeModule(module);
        }
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
