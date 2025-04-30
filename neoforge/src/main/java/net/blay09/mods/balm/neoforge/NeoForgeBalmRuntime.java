package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.*;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.component.BalmComponents;
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
import net.blay09.mods.balm.api.proxy.*;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.*;
import net.blay09.mods.balm.neoforge.block.NeoForgeBalmBlocks;
import net.blay09.mods.balm.neoforge.block.entity.NeoForgeBalmBlockEntities;
import net.blay09.mods.balm.neoforge.capability.NeoForgeBalmCapabilities;
import net.blay09.mods.balm.neoforge.command.NeoForgeBalmCommands;
import net.blay09.mods.balm.neoforge.compat.NeoForgeBalmModSupport;
import net.blay09.mods.balm.neoforge.component.NeoForgeBalmComponents;
import net.blay09.mods.balm.neoforge.config.NeoForgeBalmConfig;
import net.blay09.mods.balm.neoforge.entity.NeoForgeBalmEntities;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmCommonEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.blay09.mods.balm.neoforge.item.NeoForgeBalmItems;
import net.blay09.mods.balm.neoforge.menu.NeoForgeBalmMenus;
import net.blay09.mods.balm.neoforge.network.NeoForgeBalmNetworking;
import net.blay09.mods.balm.neoforge.particle.NeoForgeBalmParticles;
import net.blay09.mods.balm.neoforge.permission.NeoForgeBalmPermissions;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.blay09.mods.balm.neoforge.recipe.NeoForgeBalmRecipes;
import net.blay09.mods.balm.neoforge.resources.NeoForgeBalmResources;
import net.blay09.mods.balm.neoforge.sound.NeoForgeBalmSounds;
import net.blay09.mods.balm.neoforge.stats.NeoForgeBalmStats;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.util.function.Consumer;

public class NeoForgeBalmRuntime extends CommonBalmRuntime<NeoForgeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmWorldGen worldGen = new NeoForgeBalmWorldGen();
    private final BalmItems items = new NeoForgeBalmItems(legacyNamespaceResolver);
    private final BalmBlocks blocks = new NeoForgeBalmBlocks(legacyNamespaceResolver, items);
    private final BalmBlockEntities blockEntities = new NeoForgeBalmBlockEntities();
    private final NeoForgeBalmEvents events = new NeoForgeBalmEvents();
    private final BalmMenus menus = new NeoForgeBalmMenus();
    private final BalmNetworking networking = new NeoForgeBalmNetworking(legacyNamespaceResolver);
    private final BalmConfig config = new NeoForgeBalmConfig();
    private final BalmHooks hooks = new NeoForgeBalmHooks();
    private final BalmRegistries registries = new NeoForgeBalmRegistries();
    private final BalmSounds sounds = new NeoForgeBalmSounds();
    private final BalmEntities entities = new NeoForgeBalmEntities(legacyNamespaceResolver);
    private final BalmCapabilities capabilities = new NeoForgeBalmCapabilities(legacyNamespaceResolver);
    @Deprecated(forRemoval = true, since = "1.21.5")
    private final BalmProviders providers = new NeoForgeBalmProviders();
    private final BalmCommands commands = new NeoForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmStats stats = new NeoForgeBalmStats(legacyNamespaceResolver);
    private final BalmRecipes recipes = new NeoForgeBalmRecipes();
    private final BalmComponents components = new NeoForgeBalmComponents();
    private final BalmModSupport modSupport = new NeoForgeBalmModSupport(this);
    private final BalmParticles particles = new NeoForgeBalmParticles();
    private final BalmPermissions permissions = new NeoForgeBalmPermissions();
    private final BalmResources resources = new NeoForgeBalmResources();

    public NeoForgeBalmRuntime() {
        NeoForgeBalmCommonEvents.registerEvents(events);
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
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public String getModName(String modId) {
        return ModList.get().getModContainerById(modId).map(it -> it.getModInfo().getDisplayName()).orElse(modId);
    }

    @Override
    public void initializeMod(String modId, NeoForgeLoadContext context, Runnable initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.run();

        final var modBus = context.modBus();
        DeferredRegisters.register(modId, modBus);
        ModBusEventRegisters.register(modId, modBus);
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> event.addListener(reloadListener));
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> event.addListener((ResourceManagerReloadListener) reloadListener::accept));
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
    public BalmPermissions getPermissions() {
        return permissions;
    }

    @Override
    public String getPlatform() {
        return LoaderPlatforms.NEOFORGE;
    }

    @Override
    public BalmResources getResources() {
        return resources;
    }

    @Override
    public BalmEnvironment getEnvironment() {
        return switch (FMLEnvironment.dist) {
            case CLIENT -> BalmEnvironment.CLIENT;
            case DEDICATED_SERVER -> BalmEnvironment.SERVER;
        };
    }
}