package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmProxy;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.BalmRuntime;
import net.blay09.mods.balm.api.*;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.proxy.LoaderPlatforms;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.CommonBalmLootTables;
import net.blay09.mods.balm.common.CommonBalmRuntime;
import net.blay09.mods.balm.forge.block.ForgeBalmBlocks;
import net.blay09.mods.balm.forge.block.entity.ForgeBalmBlockEntities;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.command.ForgeBalmCommands;
import net.blay09.mods.balm.forge.compat.ForgeBalmModSupport;
import net.blay09.mods.balm.forge.config.ForgeBalmConfig;
import net.blay09.mods.balm.forge.entity.ForgeBalmEntities;
import net.blay09.mods.balm.forge.event.ForgeBalmCommonEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;
import net.blay09.mods.balm.forge.item.ForgeBalmItems;
import net.blay09.mods.balm.forge.menu.ForgeBalmMenus;
import net.blay09.mods.balm.forge.network.ForgeBalmNetworking;
import net.blay09.mods.balm.forge.particle.ForgeBalmParticles;
import net.blay09.mods.balm.forge.permission.ForgeBalmPermissions;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.blay09.mods.balm.forge.recipe.ForgeBalmRecipes;
import net.blay09.mods.balm.forge.sound.ForgeBalmSounds;
import net.blay09.mods.balm.forge.stats.ForgeBalmStats;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ForgeBalmRuntime extends CommonBalmRuntime {

    private final BalmWorldGen worldGen = new ForgeBalmWorldGen();
    private final BalmBlocks blocks = new ForgeBalmBlocks();
    private final BalmBlockEntities blockEntities = new ForgeBalmBlockEntities();
    private final ForgeBalmEvents events = new ForgeBalmEvents();
    private final BalmItems items = new ForgeBalmItems();
    private final BalmMenus menus = new ForgeBalmMenus();
    private final BalmNetworking networking = new ForgeBalmNetworking();
    private final BalmConfig config = new ForgeBalmConfig();
    private final BalmHooks hooks = new ForgeBalmHooks();
    private final BalmRegistries registries = new ForgeBalmRegistries();
    private final BalmSounds sounds = new ForgeBalmSounds();
    private final BalmEntities entities = new ForgeBalmEntities();
    private final BalmCapabilities capabilities = new ForgeBalmCapabilities();
    @Deprecated(forRemoval = true, since = "1.21.5")
    private final BalmProviders providers = new ForgeBalmProviders();
    private final BalmCommands commands = new ForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmStats stats = new ForgeBalmStats();
    private final BalmRecipes recipes = new ForgeBalmRecipes();
    private final BalmModSupport modSupport = new ForgeBalmModSupport(this);
    private final BalmParticles particles = new ForgeBalmParticles();
    private final BalmPermissions permissions = new ForgeBalmPermissions();

    private final List<String> addonClasses = new ArrayList<>();

    public ForgeBalmRuntime() {
        ForgeBalmCommonEvents.registerEvents(events);
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
    @Deprecated(forRemoval = true, since = "1.21.5")
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
    public BalmPermissions getPermissions() {
        return permissions;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return ModList.get().getModContainerById(modId).map(it -> it.getModInfo().getDisplayName()).orElse(modId);
    }

    @Override
    public void initializeMod(String modId, Runnable initializer) {
        ((ForgeBalmItems) items).register();
        ((ForgeBalmEntities) entities).register();
        ((ForgeBalmWorldGen) worldGen).register();
        ((ForgeBalmStats) stats).register();

        initializer.run();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener((FMLLoadCompleteEvent event) -> initializeAddons());
        DeferredRegisters.register(modId, modEventBus);
    }

    @Override
    public void initializeIfLoaded(String modId, String className) {
        if (isModLoaded(modId)) {
            addonClasses.add(className);
        }
    }

    private void initializeAddons() {
        for (String addonClass : addonClasses) {
            try {
                Class.forName(addonClass).getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> event.addListener(reloadListener));
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> event.addListener((ResourceManagerReloadListener) reloadListener::accept));
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
    public String getPlatform() {
        return LoaderPlatforms.FORGE;
    }

    @Override
    public void initializeRuntime() {
        MinecraftForge.EVENT_BUS.register(capabilities);
        super.initializeRuntime();
    }

    @Override
    public BalmEnvironment getEnvironment() {
        return switch (FMLEnvironment.dist) {
            case CLIENT -> BalmEnvironment.CLIENT;
            case DEDICATED_SERVER -> BalmEnvironment.SERVER;
        };
    }
}