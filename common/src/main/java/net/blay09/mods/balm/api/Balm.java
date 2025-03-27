package net.blay09.mods.balm.api;

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
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.api.network.BalmNetworking;
import net.blay09.mods.balm.api.particle.BalmParticles;
import net.blay09.mods.balm.api.permission.BalmPermissions;
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.function.Consumer;
import java.util.function.Function;

public class Balm {
    private static final BalmRuntime<BalmRuntimeLoadContext> runtime = BalmRuntimeSpi.create();

    public static void registerModule(BalmModule module) {
        runtime.registerModule(module);
    }

    public static void onRuntimeAvailable(Runnable callback) {
        runtime.onRuntimeAvailable(callback);
    }

    /**
     * @deprecated Use {@link #initializeMod(String, BalmRuntimeLoadContext, Runnable)} instead.
     */
    @Deprecated
    public static void initialize(String modId, BalmRuntimeLoadContext context, Runnable initializer) {
        initializeMod(modId, context, initializer);
    }

    public static void initializeMod(String modId, BalmRuntimeLoadContext context, Runnable initializer) {
        runtime.initializeMod(modId, context, initializer);
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmModule module) {
        runtime.initializeMod(modId, context, () -> registerModule(module));
    }

    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmModule... modules) {
        runtime.initializeMod(modId, context, () -> {
            for (final var module : modules) {
                registerModule(module);
            }
        });
    }

    public static boolean isModLoaded(String modId) {
        return runtime.isModLoaded(modId);
    }

    public static String getModName(String modId) {
        return runtime.getModName(modId);
    }

    public static <T> PlatformProxy<T> platformProxy() {
        return runtime.platformProxy();
    }

    public static <T> ModProxy<T> modProxy() {
        return runtime.modProxy();
    }

    public static <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        return runtime.sidedProxy(commonName, clientName);
    }

    public static void initializeIfLoaded(String modId, String className) {
        runtime.initializeIfLoaded(modId, className);
    }

    public static void addServerReloadListener(ResourceLocation identifier, Function<HolderLookup.Provider, PreparableReloadListener> reloadListener) {
        runtime.addServerReloadListener(identifier, reloadListener);
    }

    public static void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        runtime.addServerReloadListener(identifier, it -> reloadListener);
    }

    public static void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        runtime.addServerReloadListener(identifier, reloadListener);
    }

    public static BalmProxy getProxy() {
        return runtime.getProxy();
    }

    public static BalmEvents getEvents() {
        return runtime.getEvents();
    }

    public static BalmConfig getConfig() {
        return runtime.getConfig();
    }

    public static BalmNetworking getNetworking() {
        return runtime.getNetworking();
    }

    public static BalmWorldGen getWorldGen() {
        return runtime.getWorldGen();
    }

    public static BalmBlocks getBlocks() {
        return runtime.getBlocks();
    }

    public static BalmBlockEntities getBlockEntities() {
        return runtime.getBlockEntities();
    }

    public static BalmItems getItems() {
        return runtime.getItems();
    }

    public static BalmComponents getComponents() {
        return runtime.getComponents();
    }

    public static BalmMenus getMenus() {
        return runtime.getMenus();
    }

    public static BalmHooks getHooks() {
        return runtime.getHooks();
    }

    public static BalmRecipes getRecipes() {
        return runtime.getRecipes();
    }

    public static BalmRegistries getRegistries() {
        return runtime.getRegistries();
    }

    public static BalmSounds getSounds() {
        return runtime.getSounds();
    }

    public static BalmEntities getEntities() {
        return runtime.getEntities();
    }

    public static BalmCapabilities getCapabilities() {
        return runtime.getCapabilities();
    }

    public static BalmCommands getCommands() {
        return runtime.getCommands();
    }

    public static BalmLootTables getLootTables() {
        return runtime.getLootTables();
    }

    public static BalmStats getStats() {
        return runtime.getStats();
    }

    public static BalmModSupport getModSupport() {
        return runtime.getModSupport();
    }

    public static BalmParticles getParticles() {
        return runtime.getParticles();
    }

    public static BalmPermissions getPermissions() {
        return runtime.getPermissions();
    }

    public static BalmResources getResources() {
        return runtime.getResources();
    }

    public static BalmAttributes getAttributes() {
        return runtime.getAttributes();
    }

    public static String getPlatform() {
        return runtime.getPlatform();
    }

    public static BalmRuntime<? extends BalmRuntimeLoadContext> getRuntime() {
        return runtime;
    }
}
