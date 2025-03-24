package net.blay09.mods.balm.neoforge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.balm.neoforge.client.keymappings.NeoForgeBalmKeyMappings;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmModels;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmRenderers;
import net.blay09.mods.balm.neoforge.client.rendering.NeoForgeBalmTextures;
import net.blay09.mods.balm.neoforge.client.screen.NeoForgeBalmScreens;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmClientEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeoForgeBalmClientRuntime implements BalmClientRuntime<NeoForgeLoadContext> {

    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private final BalmRenderers renderers = new NeoForgeBalmRenderers();
    private final BalmTextures textures = new NeoForgeBalmTextures();
    private final BalmScreens screens = new NeoForgeBalmScreens();
    private final BalmKeyMappings keyMappings = new NeoForgeBalmKeyMappings();
    private final BalmModels models = new NeoForgeBalmModels();

    private boolean ready;

    public NeoForgeBalmClientRuntime() {
        NeoForgeBalmClientEvents.registerEvents(((NeoForgeBalmEvents) Balm.getEvents()));
    }

    @Override
    public BalmRenderers getRenderers() {
        return renderers;
    }

    @Override
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
    public void initialize(String modId, NeoForgeLoadContext context, Runnable initializer) {
        ((NeoForgeBalmRenderers) renderers).register(modId, context.modBus());
        ((NeoForgeBalmScreens) screens).register(modId, context.modBus());
        ((NeoForgeBalmModels) models).register(modId, context.modBus());
        ((NeoForgeBalmKeyMappings) keyMappings).register(modId, context.modBus());

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

    public void initializeRuntime() {
        ready = true;
        for (final var callback : initCallbacks) {
            callback.run();
        }
    }
}
