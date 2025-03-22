package net.blay09.mods.balm.api;

import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.component.BalmComponents;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Balm {
    private static final List<Runnable> initCallbacks = Collections.synchronizedList(new ArrayList<>());
    private static BalmRuntime<BalmRuntimeLoadContext> runtime;

    public static void onRuntimeAvailable(Runnable callback) {
        initCallbacks.add(callback);
        if (runtime != null) {
            callback.run();
        }
    }

    public static void initialize(String modId, BalmRuntimeLoadContext context, Runnable initializer) {
        requireRuntime().initialize(modId, context, initializer);
    }

    public static boolean isModLoaded(String modId) {
        return requireRuntime().isModLoaded(modId);
    }

    public static String getModName(String modId) {
        return requireRuntime().getModName(modId);
    }

    public static <T> PlatformProxy<T> platformProxy() {
        return requireRuntime().platformProxy();
    }

    public static <T> ModProxy<T> modProxy() {
        return requireRuntime().modProxy();
    }

    public static <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        return requireRuntime().sidedProxy(commonName, clientName);
    }

    public static void initializeIfLoaded(String modId, String className) {
        requireRuntime().initializeIfLoaded(modId, className);
    }

    public static void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        requireRuntime().addServerReloadListener(identifier, reloadListener);
    }

    public static void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        requireRuntime().addServerReloadListener(identifier, reloadListener);
    }

    public static BalmProxy getProxy() {
        return requireRuntime().getProxy();
    }

    public static BalmEvents getEvents() {
        return requireRuntime().getEvents();
    }

    public static BalmConfig getConfig() {
        return requireRuntime().getConfig();
    }

    public static BalmNetworking getNetworking() {
        return requireRuntime().getNetworking();
    }

    public static BalmWorldGen getWorldGen() {
        return requireRuntime().getWorldGen();
    }

    public static BalmBlocks getBlocks() {
        return requireRuntime().getBlocks();
    }

    public static BalmBlockEntities getBlockEntities() {
        return requireRuntime().getBlockEntities();
    }

    public static BalmItems getItems() {
        return requireRuntime().getItems();
    }

    public static BalmComponents getComponents() {
        return requireRuntime().getComponents();
    }

    public static BalmMenus getMenus() {
        return requireRuntime().getMenus();
    }

    public static BalmHooks getHooks() {
        return requireRuntime().getHooks();
    }

    public static BalmRecipes getRecipes() {
        return requireRuntime().getRecipes();
    }

    public static BalmRegistries getRegistries() {
        return requireRuntime().getRegistries();
    }

    public static BalmSounds getSounds() {
        return requireRuntime().getSounds();
    }

    public static BalmEntities getEntities() {
        return requireRuntime().getEntities();
    }

    public static BalmProviders getProviders() {
        return requireRuntime().getProviders();
    }

    public static BalmCommands getCommands() {
        return requireRuntime().getCommands();
    }

    public static BalmLootTables getLootTables() {
        return requireRuntime().getLootTables();
    }

    public static BalmStats getStats() {
        return requireRuntime().getStats();
    }

    public static BalmModSupport getModSupport() {
        return requireRuntime().getModSupport();
    }

    public static BalmParticles getParticles() {
        return requireRuntime().getParticles();
    }

    public static BalmPermissions getPermissions() {
        return requireRuntime().getPermissions();
    }

    public static String getPlatform() {
        return requireRuntime().getPlatform();
    }

    public static BalmRuntime<? extends BalmRuntimeLoadContext> getRuntime() {
        return requireRuntime();
    }

    @SuppressWarnings("unchecked")
    private static <T extends BalmRuntimeLoadContext> BalmRuntime<T> requireRuntime() {
        if (runtime == null) {
            // TODO In 1.21.5, we will only initialize the runtime at a stable and safe time, and crash if accessed too early.
            initializeRuntime();
        }
        return (BalmRuntime<T>) runtime;
    }

    public static void initializeRuntime() {
        if (runtime == null) {
            runtime = BalmRuntimeSpi.create();
            for (final var callback : initCallbacks) {
                callback.run();
            }
        }
    }
}
