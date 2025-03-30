package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.api.event.client.ClientStartedEvent;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.client.CommonBalmClientRuntime;
import net.blay09.mods.balm.fabric.FabricBalmRuntime;
import net.blay09.mods.balm.fabric.client.keymappings.FabricBalmKeyMappings;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmModels;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmRenderers;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmTextures;
import net.blay09.mods.balm.fabric.client.screen.FabricBalmScreens;
import net.blay09.mods.balm.fabric.event.FabricBalmEvents;
import net.blay09.mods.balm.fabric.event.client.FabricBalmClientEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricBalmClientRuntime extends CommonBalmClientRuntime<EmptyLoadContext> {

    private static final Logger logger = LoggerFactory.getLogger(FabricBalmClientRuntime.class);
    private final BalmRenderers renderers = new FabricBalmRenderers();
    @Deprecated(forRemoval = true, since = "1.21.5")
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
    public BalmKeyMappings getKeyMappings() {
        return keyMappings;
    }

    @Override
    public BalmModels getModels() {
        return models;
    }

    @Override
    public void initializeMod(String modId, EmptyLoadContext context, Runnable initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.run();
    }

    @Override
    public void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                return reloadListener.reload(barrier, manager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }
        });
    }
}
