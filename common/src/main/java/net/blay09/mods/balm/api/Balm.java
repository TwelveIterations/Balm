package net.blay09.mods.balm.api;

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
import net.blay09.mods.balm.api.provider.BalmProviders;
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class Balm {
    private static final BalmRuntime<BalmRuntimeLoadContext> runtime = BalmRuntimeSpi.create();

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#registerModule(BalmModule)} instead.
     */
    @Deprecated
    public static void registerModule(BalmModule module) {
        runtime.registerModule(module);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.Balmstrap#onRuntimeAvailable(Runnable)} instead.
     */
    @Deprecated
    public static void onRuntimeAvailable(Runnable callback) {
        runtime.onRuntimeAvailable(callback);
    }

    /**
     * @deprecated Use {@link #initializeMod(String, BalmRuntimeLoadContext, Runnable)} instead.
     */
    @Deprecated(since = "1.22")
    public static void initialize(String modId, BalmRuntimeLoadContext context, Runnable initializer) {
        initializeMod(modId, context, initializer);
    }

    /**
     * @deprecated Use {@link #initializeMod(String, BalmRuntimeLoadContext, Consumer)} instead.
     */
    @Deprecated
    public static void initializeMod(String modId, BalmRuntimeLoadContext context, Runnable initializer) {
        runtime.initializeMod(modId, context, initializer);
    }

    /**
     * You must call this or any of its overloads in each of your mod's entry points. Provide a load context specific to each mod loader.
     * Everything else you do in Balm should happen inside the initializer, which runs at a time that Balm has
     * initialized the runtime for your mod.
     *
     * @param modId       The mod id for your mod.
     * @param context     The load context for the mod loader you are using, e.g. NeoForgeLoadContext.
     * @param initializer Callback that runs when Balm is ready, at which point you can use its methods to set up your mod.
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule)
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule...)
     */
    public static void initializeMod(String modId, BalmRuntimeLoadContext context, Consumer<BalmRegistrars> initializer) {
        runtime.initializeMod(modId, context, initializer);
    }

    /**
     * You must call this or any of its overloads in each of your mod's entry points. Provide a load context specific to each mod loader.
     * Everything else you do in Balm should happen inside the module, whose methods are called at a time that Balm has
     * initialized the runtime for your mod.
     *
     * @param modId   the mod id for your mod.
     * @param context the load context for the mod loader you are using, e.g. NeoForgeLoadContext.
     * @param module  an implementation of {@link BalmModule} within which you can set up your mod.
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, Consumer)
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule...)
     */
    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmModule module) {
        runtime.initializeMod(modId, context, (registrars) -> registrars.registerModule(module));
    }

    /**
     * You must call this or any of its overloads in each of your mod's entry points. Provide a load context specific to each mod loader.
     * Everything else you do in Balm should happen inside the module, whose methods are called at a time that Balm has
     * initialized the runtime for your mod.
     *
     * @param modId   the mod id for your mod.
     * @param context the load context for the mod loader you are using, e.g. NeoForgeLoadContext.
     * @param modules one or more implementations of {@link BalmModule} within which you can set up your mod.
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, Consumer)
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule)
     */
    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmModule... modules) {
        runtime.initializeMod(modId, context, (registrars) -> {
            for (final var module : modules) {
                registrars.registerModule(module);
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

    /**
     * @deprecated Use {@link BalmRegistrars#resourceReloadListeners(Consumer)} instead.
     */
    @Deprecated
    public static void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        runtime.addServerReloadListener(identifier, reloadListener);
    }

    /**
     * @deprecated Use {@link BalmRegistrars#resourceReloadListeners(Consumer)} instead.
     */
    @Deprecated
    public static void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        runtime.addServerReloadListener(identifier, reloadListener);
    }

    /**
     * @deprecated Renamed to {@link #safeClientAccess()}.
     */
    @Deprecated
    public static BalmProxy getProxy() {
        return safeClientAccess();
    }

    public static BalmProxy safeClientAccess() {
        return runtime.getProxy();
    }

    public static BalmEvents getEvents() {
        return runtime.getEvents();
    }

    /**
     * @deprecated Renamed to {@link #config()}.
     */
    @Deprecated
    public static BalmConfig getConfig() {
        return config();
    }

    public static BalmConfig config() {
        return runtime.getConfig();
    }

    /**
     * @deprecated Renamed to {@link #networking()}.
     */
    @Deprecated
    public static BalmNetworking getNetworking() {
        return networking();
    }

    public static BalmNetworking networking() {
        return runtime.getNetworking();
    }

    public static BalmWorldGen getWorldGen() {
        return runtime.getWorldGen();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#blocks(Consumer)} instead.
     */
    @Deprecated
    public static BalmBlocks getBlocks() {
        return runtime.getBlocks();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#blockEntityTypes(Consumer)} instead.
     */
    @Deprecated
    public static BalmBlockEntities getBlockEntities() {
        return runtime.getBlockEntities();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#items(Consumer)} and {@link BalmRegistrars#creativeModeTabs(Consumer)} instead.
     */
    @Deprecated
    public static BalmItems getItems() {
        return runtime.getItems();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#dataComponentTypes(Consumer)} instead.
     */
    @Deprecated
    public static BalmComponents getComponents() {
        return runtime.getComponents();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#menuTypes(Consumer)} instead.
     */
    @Deprecated
    public static BalmMenus getMenus() {
        return runtime.getMenus();
    }

    /**
     * @deprecated Renamed to {@link #hooks()}.
     */
    @Deprecated
    public static BalmHooks getHooks() {
        return hooks();
    }

    public static BalmHooks hooks() {
        return runtime.getHooks();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#recipeTypes(Consumer)} instead.
     */
    @Deprecated
    public static BalmRecipes getRecipes() {
        return runtime.getRecipes();
    }

    public static BalmRegistries getRegistries() {
        return runtime.getRegistries();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#registrar(ResourceKey)} with {@link net.minecraft.core.registries.Registries#SOUND_EVENT} instead.
     */
    @Deprecated
    public static BalmSounds getSounds() {
        return runtime.getSounds();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#entityTypes(Consumer)} instead.
     */
    @Deprecated
    public static BalmEntities getEntities() {
        return runtime.getEntities();
    }

    /**
     * @deprecated Renamed to {@link #capabilities()}.
     */
    @Deprecated
    public static BalmCapabilities getCapabilities() {
        return capabilities();
    }

    public static BalmCapabilities capabilities() {
        return runtime.getCapabilities();
    }

    @Deprecated
    public static BalmCommands getCommands() {
        return commands();
    }

    public static BalmCommands commands() {
        return runtime.getCommands();
    }

    /**
     * @deprecated Renamed to {@link #lootModifiers()}.
     */
    @Deprecated
    public static BalmLootTables getLootTables() {
        return lootModifiers();
    }

    public static BalmLootTables lootModifiers() {
        return runtime.getLootTables();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#customStats(Consumer)} instead.
     */
    @Deprecated
    public static BalmStats getStats() {
        return runtime.getStats();
    }

    /**
     * @deprecated Renamed to {@link #modSupport()}.
     */
    @Deprecated
    public static BalmModSupport getModSupport() {
        return modSupport();
    }

    public static BalmModSupport modSupport() {
        return runtime.getModSupport();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#particleTypes(Consumer)} instead.
     */
    @Deprecated
    public static BalmParticles getParticles() {
        return runtime.getParticles();
    }

    /**
     * @deprecated Renamed to {@link #permissions()}.
     */
    @Deprecated
    public static BalmPermissions getPermissions() {
        return permissions();
    }

    public static BalmPermissions permissions() {
        return runtime.getPermissions();
    }

    /**
     * @deprecated Use {@link BalmRegistrars#resourceConditions(Consumer)} instead.
     */
    @Deprecated
    public static BalmResources getResources() {
        return runtime.getResources();
    }

    public static String getPlatform() {
        return runtime.getPlatform();
    }

    public static BalmEnvironment getEnvironment() {
        return runtime.getEnvironment();
    }

    /**
     * For internal use. Provides access to the runtime powering mod-loader specific functions.
     * Generally, you should not need to access the runtime directly, as all its methods are exposed on {@link Balm}.
     */
    public static BalmRuntime<? extends BalmRuntimeLoadContext> getRuntime() {
        return runtime;
    }

    public static Map<String, Path> lookupAllModPaths(String path) {
        return runtime.lookupAllModPaths(path);
    }

    public static Optional<Path> lookupModPaths(String modId, String path) {
        return runtime.lookupModPath(modId, path);
    }

    /**
     * @deprecated Use {@link #getCapabilities()} instead.
     */
    @Deprecated
    public static BalmProviders getProviders() {
        return runtime.getProviders();
    }

    public static boolean isDevelopmentEnvironment() {
        return runtime.isDevelopmentEnvironment();
    }
}
