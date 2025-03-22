package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.api.event.client.ClientStartedEvent;
import net.blay09.mods.balm.fabric.FabricBalmRuntime;
import net.blay09.mods.balm.fabric.event.FabricBalmEvents;
import net.blay09.mods.balm.fabric.event.client.FabricBalmClientEvents;
import net.blay09.mods.balm.fabric.client.keymappings.FabricBalmKeyMappings;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmModels;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmRenderers;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmTextures;
import net.blay09.mods.balm.fabric.client.screen.FabricBalmScreens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class FabricBalmClientRuntime implements BalmClientRuntime<EmptyLoadContext> {

    private static final Logger logger = LoggerFactory.getLogger(FabricBalmClientRuntime.class);

    private final BalmRenderers renderers = new FabricBalmRenderers();
    private final BalmTextures textures = new FabricBalmTextures();
    private final BalmScreens screens = new FabricBalmScreens();
    private final BalmKeyMappings keyMappings = createKeyMappingsBindings();
    private final BalmModels models = new FabricBalmModels();

    public FabricBalmClientRuntime() {
        FabricBalmClientEvents.registerEvents(((FabricBalmEvents) Balm.getEvents()));

        Balm.getEvents().onEvent(ClientStartedEvent.class, event -> {
            for (final var className : ((FabricBalmRuntime) Balm.getRuntime()).getAddonClasses()) {
                try {
                    Class.forName(className).getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
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
    public BalmKeyMappings getKeyMappings() {
        return keyMappings;
    }

    @Override
    public BalmModels getModels() {
        return models;
    }

    @Override
    public void initialize(String modId, EmptyLoadContext context, Runnable initializer) {
        initializer.run();
    }

    private static BalmKeyMappings createKeyMappingsBindings() {
        if (Balm.isModLoaded("amecs")) {
            try {
                Class.forName("de.siphalor.amecs.api.AmecsKeyBinding");
                try {
                    Class<?> amecs = Class.forName("net.blay09.mods.balm.fabric.compat.AmecsBalmKeyMappings");
                    return (BalmKeyMappings) amecs.getConstructor().newInstance();
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    logger.error("Failed to initialize amecs key mappings for Balm", e);
                }
            } catch (ClassNotFoundException ignored) {
                // we silently ignore amecs if the api is missing which would only really be the case in a devenv pulling from cursemaven
            }
        }
        return new FabricBalmKeyMappings();
    }
}
