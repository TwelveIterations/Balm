package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.resources.ModResource;
import net.blay09.mods.balm.api.resources.ModResourceVisitor;
import net.blay09.mods.balm.api.resources.PathModResource;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.*;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.command.ForgeBalmCommands;
import net.blay09.mods.balm.forge.compat.ForgeBalmModSupport;
import net.blay09.mods.balm.forge.config.ForgeBalmConfig;
import net.blay09.mods.balm.forge.core.ForgeBalmRegistrar;
import net.blay09.mods.balm.forge.entity.ForgeBalmEntities;
import net.blay09.mods.balm.forge.event.ForgeBalmCommonEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;
import net.blay09.mods.balm.forge.level.block.entity.ForgeBalmBlockEntityTypeFactory;
import net.blay09.mods.balm.forge.loader.ForgeBalmPlatform;
import net.blay09.mods.balm.forge.menu.ForgeBalmMenus;
import net.blay09.mods.balm.forge.network.ForgeBalmNetworking;
import net.blay09.mods.balm.forge.particle.ForgeBalmParticles;
import net.blay09.mods.balm.forge.permission.ForgeBalmPermissions;
import net.blay09.mods.balm.forge.recipe.ForgeBalmRecipes;
import net.blay09.mods.balm.forge.resources.ForgeBalmResources;
import net.blay09.mods.balm.forge.stats.ForgeBalmStats;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.blay09.mods.balm.forge.world.item.ForgeBalmCreativeModeTabFactory;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabFactory;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeFactory;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class ForgeBalmRuntime extends CommonBalmRuntime<ForgeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmWorldGen worldGen = new ForgeBalmWorldGen();
    private final ForgeBalmEvents events = new ForgeBalmEvents();
    private final BalmMenus menus = new ForgeBalmMenus();
    private final BalmNetworking networking = new ForgeBalmNetworking();
    private final BalmConfig config = new ForgeBalmConfig();
    private final BalmHooks hooks = new ForgeBalmHooks();
    private final BalmEntities entities = new ForgeBalmEntities(legacyNamespaceResolver);
    private final BalmCapabilities capabilities = new ForgeBalmCapabilities(legacyNamespaceResolver);
    private final BalmCommands commands = new ForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmStats stats = new ForgeBalmStats(legacyNamespaceResolver);
    private final BalmRecipes recipes = new ForgeBalmRecipes();
    private final BalmModSupport modSupport = new ForgeBalmModSupport(this);
    private final BalmParticles particles = new ForgeBalmParticles();
    private final BalmPermissions permissions = new ForgeBalmPermissions();
    private final BalmResources resources = new ForgeBalmResources();
    private final BalmRegistrar registrar = new ForgeBalmRegistrar();
    private final BalmPlatform platform = new ForgeBalmPlatform();

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
    public BalmPermissions getPermissions() {
        return permissions;
    }

    @Override
    public void initializeMod(String modId, ForgeLoadContext context, Runnable initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.run();

        final var modEventBus = context.modBusGroup();
        DeferredRegisters.register(modId, modEventBus);
        ModBusEventRegisters.register(modId, modEventBus);
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Function<HolderLookup.Provider, PreparableReloadListener> reloadListener) {
        AddReloadListenerEvent.BUS.addListener((event) -> event.addListener(reloadListener.apply(event.getRegistries())));
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        AddReloadListenerEvent.BUS.addListener((event) -> event.addListener((ResourceManagerReloadListener) reloadListener::accept));
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
    public BalmResources getResources() {
        return resources;
    }

    @Override
    public void visitModResources(String modId, String path, ModResourceVisitor visitor) {
        final var modFile = ModList.get().getModFileById(modId);
        final var nioPath = modFile.getFile().findResource(path);
        if (Files.exists(nioPath)) {
            try (final var walker = Files.walk(nioPath)) {
                walker.forEach(childPath -> visitor.visit(new PathModResource(childPath)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Optional<ModResource> lookupModResource(String modId, String path) {
        final var modFile = ModList.get().getModFileById(modId);
        final var resource = modFile.getFile().findResource(path);
        return Files.exists(resource) ? Optional.of(new PathModResource(resource)) : Optional.empty();
    }

    @Override
    public BalmRegistrar registrar() {
        return registrar;
    }

    @Override
    public void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeFactory> initializer) {
        initializer.accept(new ForgeBalmBlockEntityTypeFactory(registrar(), namespace));
    }

    @Override
    public void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabFactory> initializer) {
        initializer.accept(new ForgeBalmCreativeModeTabFactory(registrar(), namespace));
    }

    @Override
    public BalmCreativeModeTabFactory creativeModeTabs(String namespace) {
        return new ForgeBalmCreativeModeTabFactory(registrar(), namespace);
    }

    @Override
    public BalmBlockEntityTypeFactory blockEntityTypes(String namespace) {
        return new ForgeBalmBlockEntityTypeFactory(registrar(), namespace);
    }

    @Override
    public BalmPlatform platform() {
        return platform;
    }
}