package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.blay09.mods.balm.forge.client.keymappings.ForgeBalmKeyMappings;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmModels;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmRenderers;
import net.blay09.mods.balm.forge.client.screen.ForgeBalmScreens;
import net.blay09.mods.balm.forge.event.ForgeBalmClientEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForgeBalmClientRuntime implements BalmClientRuntime<ForgeLoadContext> {

    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static final List<BalmClientModule> modules = Collections.synchronizedList(new ArrayList<>());
    private final BalmRenderers renderers = new ForgeBalmRenderers();
    private final BalmScreens screens = new ForgeBalmScreens();
    private final BalmKeyMappings keyMappings = new ForgeBalmKeyMappings();
    private final BalmModels models = new ForgeBalmModels();

    private boolean ready;

    public ForgeBalmClientRuntime() {
        ForgeBalmClientEvents.registerEvents(((ForgeBalmEvents) Balm.getEvents()));
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
    public void initializeMod(String modId, ForgeLoadContext context, Runnable initializer) {
        BalmLoadContexts.register(modId, context);

        ((ForgeBalmRenderers) renderers).register(modId, context.modEventBus());
        ((ForgeBalmScreens) screens).register(modId, context.modEventBus());
        ((ForgeBalmModels) models).register(modId, context.modEventBus());
        ((ForgeBalmKeyMappings) keyMappings).register(modId, context.modEventBus());

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
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager reloadableResourceManager) {
            reloadableResourceManager.registerReloadListener(reloadListener);
        }
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
}
