package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.api.event.client.ClientStartedEvent;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.fabric.FabricBalmRuntime;
import net.blay09.mods.balm.fabric.client.keymappings.FabricBalmKeyMappings;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmModels;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmRenderers;
import net.blay09.mods.balm.fabric.client.screen.FabricBalmScreens;
import net.blay09.mods.balm.fabric.event.FabricBalmEvents;
import net.blay09.mods.balm.fabric.event.client.FabricBalmClientEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricBalmClientRuntime implements BalmClientRuntime<EmptyLoadContext> {

    private static final Logger logger = LoggerFactory.getLogger(FabricBalmClientRuntime.class);
    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static final List<BalmClientModule> modules = Collections.synchronizedList(new ArrayList<>());
    private final BalmRenderers renderers = new FabricBalmRenderers();
    private final BalmScreens screens = new FabricBalmScreens();
    private final BalmKeyMappings keyMappings = new FabricBalmKeyMappings();
    private final BalmModels models = new FabricBalmModels();

    private boolean ready;

    public FabricBalmClientRuntime() {
        FabricBalmClientEvents.registerEvents(((FabricBalmEvents) Balm.getEvents()));

        Balm.getEvents().onEvent(ClientStartedEvent.class, event -> {
            for (final var className : ((FabricBalmRuntime) Balm.getRuntime()).getAddonClasses()) {
                try {
                    Class.forName(className).getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                    logger.error("Failed to initialize addon class '{}'", className, e);
                }
            }
        });
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
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, Executor backgroundExecutor, Executor gameExecutor) {
                return reloadListener.reload(barrier, manager, backgroundExecutor, gameExecutor);
            }
        });
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
