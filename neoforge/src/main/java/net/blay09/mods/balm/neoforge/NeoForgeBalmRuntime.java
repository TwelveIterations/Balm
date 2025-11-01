package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
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
import net.blay09.mods.balm.api.proxy.LoaderPlatforms;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.resources.ModResource;
import net.blay09.mods.balm.api.resources.ModResourceVisitor;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.common.*;
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.neoforge.block.entity.NeoForgeBalmBlockEntities;
import net.blay09.mods.balm.neoforge.capability.NeoForgeBalmCapabilities;
import net.blay09.mods.balm.neoforge.command.NeoForgeBalmCommands;
import net.blay09.mods.balm.neoforge.compat.NeoForgeBalmModSupport;
import net.blay09.mods.balm.neoforge.component.NeoForgeBalmComponents;
import net.blay09.mods.balm.neoforge.config.NeoForgeBalmConfig;
import net.blay09.mods.balm.neoforge.entity.NeoForgeBalmEntities;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmCommonEvents;
import net.blay09.mods.balm.neoforge.event.NeoForgeBalmEvents;
import net.blay09.mods.balm.neoforge.menu.NeoForgeBalmMenus;
import net.blay09.mods.balm.neoforge.network.NeoForgeBalmNetworking;
import net.blay09.mods.balm.neoforge.particle.NeoForgeBalmParticles;
import net.blay09.mods.balm.neoforge.permission.NeoForgeBalmPermissions;
import net.blay09.mods.balm.neoforge.recipe.NeoForgeBalmRecipes;
import net.blay09.mods.balm.neoforge.core.NeoForgeBalmRegistrar;
import net.blay09.mods.balm.neoforge.resources.NeoForgeBalmResources;
import net.blay09.mods.balm.neoforge.resources.NeoForgeModResource;
import net.blay09.mods.balm.neoforge.sound.NeoForgeBalmSounds;
import net.blay09.mods.balm.neoforge.stats.NeoForgeBalmStats;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.blay09.mods.balm.neoforge.world.item.NeoForgeBalmCreativeModeTabFactory;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabFactory;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforgespi.language.IModInfo;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class NeoForgeBalmRuntime extends CommonBalmRuntime<NeoForgeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmWorldGen worldGen = new NeoForgeBalmWorldGen();
    private final BalmBlockEntities blockEntities = new NeoForgeBalmBlockEntities();
    private final NeoForgeBalmEvents events = new NeoForgeBalmEvents();
    private final BalmMenus menus = new NeoForgeBalmMenus();
    private final BalmNetworking networking = new NeoForgeBalmNetworking(legacyNamespaceResolver);
    private final BalmConfig config = new NeoForgeBalmConfig();
    private final BalmHooks hooks = new NeoForgeBalmHooks();
    private final BalmSounds sounds = new NeoForgeBalmSounds();
    private final BalmEntities entities = new NeoForgeBalmEntities(legacyNamespaceResolver);
    private final BalmCapabilities capabilities = new NeoForgeBalmCapabilities(legacyNamespaceResolver);
    private final BalmCommands commands = new NeoForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    private final BalmStats stats = new NeoForgeBalmStats(legacyNamespaceResolver);
    private final BalmRecipes recipes = new NeoForgeBalmRecipes();
    private final BalmComponents components = new NeoForgeBalmComponents();
    private final BalmModSupport modSupport = new NeoForgeBalmModSupport(this);
    private final BalmParticles particles = new NeoForgeBalmParticles();
    private final BalmPermissions permissions = new NeoForgeBalmPermissions();
    private final BalmResources resources = new NeoForgeBalmResources();
    private final BalmRegistrar registrar = new NeoForgeBalmRegistrar();

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
    public BalmBlockEntities getBlockEntities() {
        return blockEntities;
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
    public void addServerReloadListener(ResourceLocation identifier, Function<HolderLookup.Provider, PreparableReloadListener> reloadListener) {
        NeoForge.EVENT_BUS.addListener((AddServerReloadListenersEvent event) -> event.addListener(identifier, reloadListener.apply(event.getRegistryAccess())));
    }

    @Override
    public void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        NeoForge.EVENT_BUS.addListener((AddServerReloadListenersEvent event) -> event.addListener(identifier,
                (ResourceManagerReloadListener) reloadListener::accept));
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
        return switch (FMLEnvironment.getDist()) {
            case CLIENT -> BalmEnvironment.CLIENT;
            case DEDICATED_SERVER -> BalmEnvironment.DEDICATED_SERVER;
        };
    }

    @Override
    public List<String> getLoadedPrimaryModIds() {
        return ModList.get().getMods().stream().map(IModInfo::getModId).toList();
    }

    @Override
    public void visitModResources(String modId, String path, ModResourceVisitor visitor) {
        final var modFile = ModList.get().getModFileById(modId);
        if (modFile != null) {
            modFile.getFile().getContents().visitContent(path, (relativePath, resource) -> visitor.visit(new NeoForgeModResource(relativePath, resource.retain())));
        }
    }

    @Override
    public Optional<ModResource> lookupModResource(String modId, String path) {
        return Optional.ofNullable(ModList.get().getModFileById(modId))
                .map(it -> it.getFile().getContents().get(path))
                .map(it -> new NeoForgeModResource(path, it.retain()));
    }

    @Override
    public BalmRegistrar registrar() {
        return registrar;
    }

    @Override
    public BalmCreativeModeTabFactory creativeModeTabs(String namespace) {
        return new NeoForgeBalmCreativeModeTabFactory(registrar(), namespace);
    }
}