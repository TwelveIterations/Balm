package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.attribute.BalmAttributes;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.event.server.ServerStartingEvent;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.proxy.*;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.blay09.mods.balm.common.CommonBalmRuntime;
import net.blay09.mods.balm.common.permission.CommonBalmPermissions;
import net.blay09.mods.balm.common.proxy.ModProxyImpl;
import net.blay09.mods.balm.common.proxy.PlatformProxyImpl;
import net.blay09.mods.balm.fabric.attribute.FabricBalmAttributes;
import net.blay09.mods.balm.fabric.block.FabricBalmBlocks;
import net.blay09.mods.balm.fabric.block.entity.FabricBalmBlockEntities;
import net.blay09.mods.balm.fabric.capability.FabricBalmCapabilities;
import net.blay09.mods.balm.fabric.command.FabricBalmCommands;
import net.blay09.mods.balm.fabric.compat.FabricBalmModSupport;
import net.blay09.mods.balm.fabric.component.FabricBalmComponents;
import net.blay09.mods.balm.fabric.config.FabricBalmConfig;
import net.blay09.mods.balm.fabric.entity.FabricBalmEntities;
import net.blay09.mods.balm.fabric.event.FabricBalmCommonEvents;
import net.blay09.mods.balm.fabric.event.FabricBalmEvents;
import net.blay09.mods.balm.fabric.item.FabricBalmItems;
import net.blay09.mods.balm.fabric.menu.FabricBalmMenus;
import net.blay09.mods.balm.fabric.network.FabricBalmNetworking;
import net.blay09.mods.balm.fabric.particle.FabricBalmParticles;
import net.blay09.mods.balm.fabric.recipe.FabricBalmRecipes;
import net.blay09.mods.balm.fabric.resources.FabricBalmResources;
import net.blay09.mods.balm.fabric.sound.FabricBalmSounds;
import net.blay09.mods.balm.fabric.stats.FabricBalmStats;
import net.blay09.mods.balm.fabric.world.FabricBalmWorldGen;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public class FabricBalmRuntime extends CommonBalmRuntime<EmptyLoadContext> {
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
    private final BalmCapabilities capabilities = new FabricBalmCapabilities();
    private final BalmCommands commands = new FabricBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmStats stats = new FabricBalmStats();
    private final BalmRecipes recipes = new FabricBalmRecipes();
    private final BalmComponents components = new FabricBalmComponents();
    private final BalmModSupport modSupport = new FabricBalmModSupport(this);
    private final BalmParticles particles = new FabricBalmParticles();
    private final BalmPermissions permissions = this.<BalmPermissions>modProxy()
            .with("fabric-permissions-api-v0", "net.blay09.mods.balm.fabric.compat.FabricPermissionsAPIIntegration")
            .withFallback(new CommonBalmPermissions())
            .build();
    private final BalmResources resources = new FabricBalmResources();
    private final BalmAttributes attributes = new FabricBalmAttributes();

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
    public BalmCapabilities getCapabilities() {
        return capabilities;
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
    public void initializeMod(String modId, EmptyLoadContext context, Runnable initializer) {
        initializer.run();
    }

    @Override
    public void initializeIfLoaded(String modId, String className) {
        if (isModLoaded(modId)) {
            addonClasses.add(className);
        }
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Function<HolderLookup.Provider, PreparableReloadListener> factory) {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(identifier, providers -> new IdentifiableResourceReloadListener() {
            private final PreparableReloadListener listener = factory.apply(providers);

            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, Executor executor, Executor executor2) {
                return listener.reload(preparationBarrier, resourceManager, executor, executor2);
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

    @Override
    public <T> PlatformProxy<T> platformProxy() {
        return new PlatformProxyImpl<>(LoaderPlatforms.FABRIC);
    }

    @Override
    public BalmPermissions getPermissions() {
        return permissions;
    }

    @Override
    public <T> ModProxy<T> modProxy() {
        return new ModProxyImpl<>(this::isModLoaded);
    }

    @Override
    public String getPlatform() {
        return LoaderPlatforms.FABRIC;
    }

    public List<String> getAddonClasses() {
        return addonClasses;
    }

    @Override
    public BalmResources getResources() {
        return resources;
    }

    @Override
    public BalmAttributes getAttributes() {
        return attributes;
    }
}
