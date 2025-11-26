package net.blay09.mods.balm.forge;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.balm.api.BalmHooks;
import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
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
import net.blay09.mods.balm.forge.block.ForgeBalmBlocks;
import net.blay09.mods.balm.forge.block.entity.ForgeBalmBlockEntities;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.command.ForgeBalmCommands;
import net.blay09.mods.balm.forge.compat.ForgeBalmModSupport;
import net.blay09.mods.balm.forge.component.ForgeBalmComponents;
import net.blay09.mods.balm.forge.config.ForgeBalmConfig;
import net.blay09.mods.balm.forge.core.ForgeBalmRegistrar;
import net.blay09.mods.balm.forge.core.particles.ForgeBalmParticleTypeRegistrar;
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
import net.blay09.mods.balm.forge.resources.ForgeBalmResources;
import net.blay09.mods.balm.forge.server.packs.resources.ForgeBalmResourceConditionRegistrar;
import net.blay09.mods.balm.forge.server.packs.resources.ForgeBalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.forge.sound.ForgeBalmSounds;
import net.blay09.mods.balm.forge.stats.ForgeBalmCustomStatRegistrar;
import net.blay09.mods.balm.forge.stats.ForgeBalmStats;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.blay09.mods.balm.forge.world.block.entity.ForgeBalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.forge.world.entity.ForgeBalmEntityTypeRegistrar;
import net.blay09.mods.balm.forge.world.inventory.ForgeBalmMenuTypeRegistrar;
import net.blay09.mods.balm.forge.world.item.ForgeBalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("removal")
public class ForgeBalmRuntime extends CommonBalmRuntime<BalmRuntimeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmWorldGen worldGen = new ForgeBalmWorldGen();
    @Deprecated
    private final BalmItems items = new ForgeBalmItems(legacyNamespaceResolver);
    @Deprecated
    private final BalmBlocks blocks = new ForgeBalmBlocks(legacyNamespaceResolver, items);
    @Deprecated
    private final BalmBlockEntities blockEntities = new ForgeBalmBlockEntities();
    private final ForgeBalmEvents events = new ForgeBalmEvents();
    @Deprecated
    private final BalmMenus menus = new ForgeBalmMenus();
    private final BalmNetworking networking = new ForgeBalmNetworking();
    private final BalmConfig config = new ForgeBalmConfig();
    private final BalmHooks hooks = new ForgeBalmHooks();
    private final BalmRegistries registries = new ForgeBalmRegistries();
    @Deprecated
    private final BalmSounds sounds = new ForgeBalmSounds();
    @Deprecated
    private final BalmEntities entities = new ForgeBalmEntities(legacyNamespaceResolver);
    private final BalmCapabilities capabilities = new ForgeBalmCapabilities(legacyNamespaceResolver);
    @Deprecated(since = "1.21.5")
    private final BalmProviders providers = new ForgeBalmProviders();
    private final BalmCommands commands = new ForgeBalmCommands();
    private final BalmLootTables lootTables = new CommonBalmLootTables();
    @Deprecated
    private final BalmStats stats = new ForgeBalmStats(legacyNamespaceResolver);
    @Deprecated
    private final BalmRecipes recipes = new ForgeBalmRecipes();
    @Deprecated
    private final BalmComponents components = new ForgeBalmComponents();
    private final BalmModSupport modSupport = new ForgeBalmModSupport(this);
    @Deprecated
    private final BalmParticles particles = new ForgeBalmParticles();
    private final BalmPermissions permissions = new ForgeBalmPermissions();
    private final BalmRegistrar registrar = new ForgeBalmRegistrar();
    @Deprecated
    private final BalmResources resources = new ForgeBalmResources();

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
    @Deprecated(since = "1.21.5")
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
    public void initializeMod(String modId, BalmRuntimeLoadContext context, Consumer<BalmRegistrars> initializer) {
        ForgeLoadContext forgeLoadContext;
        if (context instanceof ForgeLoadContext) {
            forgeLoadContext = (ForgeLoadContext) context;
        } else {
            forgeLoadContext = new ForgeLoadContext(FMLJavaModLoadingContext.get().getModEventBus());
        }
        BalmLoadContexts.register(modId, forgeLoadContext);

        initializer.accept(new BalmRegistrars(this, modId));

        final var modEventBus = forgeLoadContext.modEventBus();
        DeferredRegisters.register(modId, modEventBus);
        ModBusEventRegisters.register(modId, modEventBus);
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
    public String getPlatform() {
        return LoaderPlatforms.FORGE;
    }

    @Override
    public void initializeRuntime() {
        MinecraftForge.EVENT_BUS.register(capabilities);
        super.initializeRuntime();
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
    public boolean isDevelopmentEnvironment() {
        return SharedConstants.IS_RUNNING_IN_IDE;
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
    public void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeRegistrar> initializer) {
        initializer.accept(new ForgeBalmBlockEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void entityTypes(String namespace, Consumer<BalmEntityTypeRegistrar> initializer) {
        initializer.accept(new ForgeBalmEntityTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabRegistrar> initializer) {
        initializer.accept(new ForgeBalmCreativeModeTabRegistrar(registrar(), namespace));
    }

    @Override
    public void menuTypes(String namespace, Consumer<BalmMenuTypeRegistrar> initializer) {
        initializer.accept(new ForgeBalmMenuTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void customStats(String namespace, Consumer<BalmCustomStatRegistrar> initializer) {
        initializer.accept(new ForgeBalmCustomStatRegistrar(registrar(), namespace));
    }

    @Override
    public void particleTypes(String namespace, Consumer<BalmParticleTypeRegistrar> initializer) {
        initializer.accept(new ForgeBalmParticleTypeRegistrar(registrar(), namespace));
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmResourceReloadListenerRegistrar> initializer) {
        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> initializer.accept(new ForgeBalmResourceReloadListenerRegistrar(event)));
    }

    @Override
    public void resourceConditions(String namespace, Consumer<BalmResourceConditionRegistrar> initializer) {
        initializer.accept(new ForgeBalmResourceConditionRegistrar(namespace));
    }
}