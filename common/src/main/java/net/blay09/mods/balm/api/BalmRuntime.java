package net.blay09.mods.balm.api;

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
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface BalmRuntime {
    BalmConfig getConfig();

    BalmEvents getEvents();

    BalmWorldGen getWorldGen();

    BalmBlocks getBlocks();

    BalmBlockEntities getBlockEntities();

    BalmItems getItems();

    BalmMenus getMenus();

    BalmNetworking getNetworking();

    BalmHooks getHooks();

    BalmRegistries getRegistries();

    BalmSounds getSounds();

    BalmEntities getEntities();

    BalmCapabilities getCapabilities();

    /**
     * @deprecated Use {@link #getCapabilities()} instead.
     */
    @Deprecated(since = "1.21.5")
    BalmProviders getProviders();

    BalmCommands getCommands();

    BalmLootTables getLootTables();

    BalmStats getStats();

    BalmRecipes getRecipes();

    BalmModSupport getModSupport();

    BalmParticles getParticles();

    BalmPermissions getPermissions();

    boolean isModLoaded(String modId);

    String getModName(String modId);

    <T> SidedProxy<T> sidedProxy(String commonName, String clientName);

    void initializeMod(String modId, BalmRuntimeLoadContext context, Runnable initializer);

    void initializeIfLoaded(String modId, String className);

    void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener);

    void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener);

    <T> PlatformProxy<T> platformProxy();

    <T> ModProxy<T> modProxy();

    <T> ModProxy<T> modProxy(ResourceLocation identifier);

    String getPlatform();

    default void initializeModule(BalmModule module) {
        final var modId = module.getId().getNamespace();
        module.registerConfig(getConfig());
        module.registerAdditional(getRegistries());
        module.registerBlocks(getBlocks().scoped(modId));
        module.registerBlockEntities(getBlockEntities());
        module.registerItems(getItems().scoped(modId));
        module.registerEntities(getEntities());
        module.registerWorldGen(getWorldGen());
        module.registerNetworking(getNetworking());
        module.registerMenus(getMenus());
        module.registerCapabilities(getCapabilities());
        module.registerCommands(getCommands());
        module.registerRecipes(getRecipes());
        module.registerLootTables(getLootTables());
        module.registerStats(getStats());
        module.registerSounds(getSounds());
        module.registerPermissions(getPermissions());
        module.registerParticles(getParticles());
        module.registerEvents(getEvents());
        module.initialize();
    }

    BalmProxy getProxy();

    boolean isReady();

    void onRuntimeAvailable(Runnable callback);

    void registerModule(BalmModule module);

    BalmEnvironment getEnvironment();

    boolean isDevelopmentEnvironment();

    Map<String, Path> lookupAllModPaths(String path);

    Optional<Path> lookupModPath(String modId, String path);
}
