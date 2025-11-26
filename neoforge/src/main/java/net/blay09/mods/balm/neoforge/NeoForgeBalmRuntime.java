package net.blay09.mods.balm.neoforge;

import com.mojang.datafixers.util.Pair;
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
import net.blay09.mods.balm.api.proxy.LoaderPlatforms;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.*;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.neoforge.block.NeoForgeBalmBlocks;
import net.blay09.mods.balm.neoforge.block.entity.NeoForgeBalmBlockEntities;
import net.blay09.mods.balm.neoforge.capability.NeoForgeBalmCapabilities;
import net.blay09.mods.balm.neoforge.command.NeoForgeBalmCommands;
import net.blay09.mods.balm.neoforge.compat.NeoForgeBalmModSupport;
import net.blay09.mods.balm.neoforge.component.NeoForgeBalmComponents;
import net.blay09.mods.balm.neoforge.config.NeoForgeBalmConfig;
import net.blay09.mods.balm.neoforge.core.internal.NeoForgeBalmRegistrar;
import net.blay09.mods.balm.neoforge.core.particles.internal.NeoForgeBalmParticleTypeRegistrar;
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
import net.blay09.mods.balm.neoforge.stats.internal.NeoForgeBalmCustomStatRegistrar;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.blay09.mods.balm.neoforge.world.entity.internal.NeoForgeBalmEntityTypeRegistrar;
import net.blay09.mods.balm.neoforge.world.inventory.internal.NeoForgeBalmMenuTypeRegistrar;
import net.blay09.mods.balm.neoforge.world.item.internal.NeoForgeBalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.neoforge.world.level.block.entity.internal.NeoForgeBalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.server.packs.resources.internal.NeoForgeBalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.internal.NeoForgeBalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NeoForgeBalmRuntime extends CommonBalmRuntime<NeoForgeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmWorldGen worldGen = new NeoForgeBalmWorldGen();
    @Deprecated
    private final BalmItems items = new NeoForgeBalmItems(legacyNamespaceResolver);
    @Deprecated
    private final BalmBlocks blocks = new NeoForgeBalmBlocks(legacyNamespaceResolver, items);
    @Deprecated
    private final BalmBlockEntities blockEntities = new NeoForgeBalmBlockEntities();
    private final NeoForgeBalmEvents events = new NeoForgeBalmEvents();
    @Deprecated
    private final BalmMenus menus = new NeoForgeBalmMenus();
    private final BalmNetworking networking = new NeoForgeBalmNetworking(legacyNamespaceResolver);
    private final BalmConfig config = new NeoForgeBalmConfig();
    private final BalmHooks hooks = new NeoForgeBalmHooks();
    private final BalmRegistries registries = new NeoForgeBalmRegistries();
    @Deprecated
    private final BalmSounds sounds = new NeoForgeBalmSounds();
    @Deprecated
    private final BalmEntities entities = new NeoForgeBalmEntities(legacyNamespaceResolver);
    private final BalmCapabilities capabilities = new NeoForgeBalmCapabilities(legacyNamespaceResolver);
    @Deprecated
    private final BalmProviders providers = new NeoForgeBalmProviders();
    private final BalmCommands commands = new NeoForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    @Deprecated
    private final BalmStats stats = new NeoForgeBalmStats(legacyNamespaceResolver);
    @Deprecated
    private final BalmRecipes recipes = new NeoForgeBalmRecipes();
    @Deprecated
    private final BalmComponents components = new NeoForgeBalmComponents();
    private final BalmModSupport modSupport = new NeoForgeBalmModSupport(this);
    @Deprecated
    private final BalmParticles particles = new NeoForgeBalmParticles();
    private final BalmPermissions permissions = new NeoForgeBalmPermissions();
    private final BalmRegistrar registrar = new NeoForgeBalmRegistrar();
    @Deprecated
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
    @Deprecated
    public BalmBlocks getBlocks() {
        return blocks;
    }

    @Override
    @Deprecated
    public BalmBlockEntities getBlockEntities() {
        return blockEntities;
    }

    @Override
    @Deprecated
    public BalmItems getItems() {
        return items;
    }

    @Override
    @Deprecated
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
    @Deprecated
    public BalmSounds getSounds() {
        return sounds;
    }

    @Override
    @Deprecated
    public BalmEntities getEntities() {
        return entities;
    }

    @Override
    public BalmCapabilities getCapabilities() {
        return capabilities;
    }

    @Override
    @Deprecated
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
    @Deprecated
    public BalmStats getStats() {
        return stats;
    }

    @Override
    @Deprecated
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
    public void initializeMod(String modId, NeoForgeLoadContext context, Consumer<BalmRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmRegistrars(this, modId));

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
    @Deprecated
    public BalmComponents getComponents() {
        return components;
    }

    @Override
    public BalmModSupport getModSupport() {
        return modSupport;
    }

    @Override
    @Deprecated
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
    @Deprecated
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

    @Override
    public Map<String, Path> lookupAllModPaths(String path) {
        return ModList.get().getMods().stream()
                .map(it -> new Pair<>(it.getModId(), it.getOwningFile().getFile().findResource(path)))
                .filter(it -> Files.exists(it.getSecond()))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    @Override
    public Optional<Path> lookupModPath(String modId, String path) {
        final var modFile = ModList.get().getModFileById(modId);
        final var nioPath = modFile.getFile().findResource(path);
        return Files.exists(nioPath) ? Optional.of(nioPath) : Optional.empty();
    }

    @Override
    public BalmRegistrar registrar() {
        return registrar;
    }

    @Override
    public void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmCreativeModeTabRegistrar(registrar(), namespace));
    }

    public void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmBlockEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void entityTypes(String namespace, Consumer<BalmEntityTypeRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void menuTypes(String namespace, Consumer<BalmMenuTypeRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmMenuTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void particleTypes(String namespace, Consumer<BalmParticleTypeRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmParticleTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void customStats(String namespace, Consumer<BalmCustomStatRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmCustomStatRegistrar(registrar(), namespace));
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmResourceReloadListenerRegistrar> initializer) {
        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) ->
                initializer.accept(new NeoForgeBalmResourceReloadListenerRegistrar(event)));
    }

    @Override
    public void resourceConditions(String namespace, Consumer<BalmResourceConditionRegistrar> initializer) {
        initializer.accept(new NeoForgeBalmResourceConditionRegistrar(namespace));
    }
}