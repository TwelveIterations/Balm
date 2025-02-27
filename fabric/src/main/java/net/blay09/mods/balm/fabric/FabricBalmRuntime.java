package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.client.ClientStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.balm.api.event.server.ServerStartingEvent;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.fabric.component.FabricBalmComponents;
import net.blay09.mods.balm.fabric.compat.FabricBalmModSupport;
import net.blay09.mods.balm.fabric.event.FabricBalmCommonEvents;
import net.blay09.mods.balm.fabric.event.FabricBalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.proxy.ProxyResolutionException;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.fabric.block.FabricBalmBlocks;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.fabric.block.entity.FabricBalmBlockEntities;
import net.blay09.mods.balm.fabric.command.FabricBalmCommands;
import net.blay09.mods.balm.fabric.config.FabricBalmConfig;
import net.blay09.mods.balm.fabric.entity.FabricBalmEntities;
import net.blay09.mods.balm.fabric.item.FabricBalmItems;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.blay09.mods.balm.fabric.menu.FabricBalmMenus;
import net.blay09.mods.balm.fabric.network.FabricBalmNetworking;
import net.blay09.mods.balm.fabric.particle.FabricBalmParticles;
import net.blay09.mods.balm.fabric.provider.FabricBalmProviders;
import net.blay09.mods.balm.fabric.recipe.FabricBalmRecipes;
import net.blay09.mods.balm.fabric.sound.FabricBalmSounds;
import net.blay09.mods.balm.fabric.stats.FabricBalmStats;
import net.blay09.mods.balm.fabric.world.FabricBalmWorldGen;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class FabricBalmRuntime implements BalmRuntime<EmptyLoadContext> {
    private final BalmWorldGen worldGen = new FabricBalmWorldGen();
    private final BalmBlocks blocks = new FabricBalmBlocks();
    private final BalmBlockEntities blockEntities = new FabricBalmBlockEntities();
    private final FabricBalmEvents events = new FabricBalmEvents();
    private final BalmItems items = new FabricBalmItems();
    private final BalmMenus menus = new FabricBalmMenus();
    private final BalmNetworking networking = new FabricBalmNetworking();
    private final BalmConfig config = new FabricBalmConfig();
    private final BalmHooks hooks = new FabricBalmHooks();
    private final BalmRegistries registries = new FabricBalmRegistries();
    private final BalmSounds sounds = new FabricBalmSounds();
    private final BalmEntities entities = new FabricBalmEntities();
    private final BalmProviders providers = new FabricBalmProviders();
    private final BalmCommands commands = new FabricBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmStats stats = new FabricBalmStats();
    private final BalmRecipes recipes = new FabricBalmRecipes();
    private final BalmComponents components = new FabricBalmComponents();
    private final BalmModSupport modSupport = new FabricBalmModSupport();
    private final BalmParticles particles = new FabricBalmParticles();

    private final List<String> addonClasses = new ArrayList<>();

    public FabricBalmRuntime() {
        FabricBalmCommonEvents.registerEvents(events);

        events.onEvent(ServerStartingEvent.class, event -> {
            if (event.getServer().isDedicatedServer()) {
                for (final var className : addonClasses) {
                    try {
                        Class.forName(className).getConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public BalmConfig getConfig() {
        return config;
    }

    @Override
    public BalmEvents getEvents() {
        return events;
    }

    @Override
    public BalmWorldGen getWorldGen() {
        return worldGen;
    }

    @Override
    public BalmBlocks getBlocks() {
        return blocks;
    }

    @Override
    public BalmBlockEntities getBlockEntities() {
        return blockEntities;
    }

    @Override
    public BalmItems getItems() {
        return items;
    }

    @Override
    public BalmMenus getMenus() {
        return menus;
    }

    @Override
    public BalmNetworking getNetworking() {
        return networking;
    }

    @Override
    public BalmHooks getHooks() {
        return hooks;
    }

    @Override
    public BalmRegistries getRegistries() {
        return registries;
    }

    @Override
    public BalmSounds getSounds() {
        return sounds;
    }

    @Override
    public BalmEntities getEntities() {
        return entities;
    }

    @Override
    public BalmProviders getProviders() {
        return providers;
    }

    @Override
    public BalmCommands getCommands() {
        return commands;
    }

    @Override
    public BalmLootTables getLootTables() {
        return lootTables;
    }

    @Override
    public BalmStats getStats() {
        return stats;
    }

    @Override
    public BalmRecipes getRecipes() {
        return recipes;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return FabricLoader.getInstance().getModContainer(modId).map(it -> it.getMetadata().getName()).orElse(modId);
    }

    @Override
    public <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        SidedProxy<T> proxy = new SidedProxy<>(commonName, clientName);
        try {
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                proxy.resolveClient();
            } else {
                proxy.resolveCommon();
            }
        } catch (ProxyResolutionException e) {
            throw new RuntimeException(e);
        }

        return proxy;
    }

    @Override
    public void initialize(String modId, EmptyLoadContext context, Runnable initializer) {
        initializer.run();
    }

    @Override
    public void initializeIfLoaded(String modId, String className) {
        if (isModLoaded(modId)) {
            addonClasses.add(className);
        }
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
                return reloadListener.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
            }
        });
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                reloadListener.accept(resourceManager);
            }

            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }
        });
    }

    @Override
    public BalmComponents getComponents() {
        return components;
    }

    @Override
    public BalmModSupport getModSupport() {
        return modSupport;
    }

    @Override
    public BalmParticles getParticles() {
        return particles;
    }

    public List<String> getAddonClasses() {
        return addonClasses;
    }
}
