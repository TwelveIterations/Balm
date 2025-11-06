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
import net.blay09.mods.balm.api.proxy.ModProxy;
import net.blay09.mods.balm.api.proxy.PlatformProxy;
import net.blay09.mods.balm.api.proxy.SidedProxy;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.api.resources.BalmResources;
import net.blay09.mods.balm.api.resources.ModResource;
import net.blay09.mods.balm.api.resources.ModResourceVisitor;
import net.blay09.mods.balm.api.sound.BalmSounds;
import net.blay09.mods.balm.api.stats.BalmStats;
import net.blay09.mods.balm.api.world.BalmWorldGen;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Provides access to common registry functions as well as various loader-specific utilities.
 * <p>
 * To initialize your mod with Balm, use {@link #initializeMod(String, BalmRuntimeLoadContext, BalmModule)} or its overloads,
 * passing either an implementation of {@link BalmModule} or a {@link Runnable}.
 * <p>
 * If you are using client-side features of Balm, you must also initialize the client-side runtime
 * using {@link net.blay09.mods.balm.api.client.BalmClient#initializeMod(String, BalmRuntimeLoadContext, net.blay09.mods.balm.api.client.module.BalmClientModule)} or its overloads.
 *
 * @see net.blay09.mods.balm.api.client.BalmClient
 */
public class Balm {
    private static final BalmRuntime<BalmRuntimeLoadContext> runtime = BalmRuntimeSpi.create();

    /**
     * Not to be confused with {@link #initializeMod(String, BalmRuntimeLoadContext, BalmModule)}, which should be used
     * for registering your mod with Balm. This method registers an additional module and should only be called from an
     * initializer or entrypoint. Some things may not work as expected if you try to register a module before
     * <code>initializeMod</code> has been called.
     *
     * @param module the module to register for an already initialized mod.
     * @see #initializeMod(String, BalmRuntimeLoadContext, BalmModule)
     */
    public static void registerModule(BalmModule module) {
        runtime.registerModule(module);
    }

    /**
     * Register a callback to run when Balm is ready. This is for third party mods that do not use Balm but want to interact with it.
     * <p>
     * Mods building on Balm should use {@link #initializeMod(String, BalmRuntimeLoadContext, Runnable)} instead.
     *
     * @param callback the callback to run when Balm is ready and its methods can be safely accessed.
     * @see #initializeMod(String, BalmRuntimeLoadContext, Runnable)
     */
    public static void onRuntimeAvailable(Runnable callback) {
        runtime.onRuntimeAvailable(callback);
    }

    /**
     * You must call this or any of its overloads in each of your mod's entry points. Provide a load context specific to each mod loader.
     * Everything else you do in Balm should happen inside the initializer, which runs at a time that Balm has
     * initialized the runtime for your mod.
     *
     * @deprecated Use the variant that takes a {@link Consumer} instead, giving you access to {@link BalmRegistrars}.
     * @param modId       The mod id for your mod.
     * @param context     The load context for the mod loader you are using, e.g. NeoForgeLoadContext.
     * @param initializer Callback that runs when Balm is ready, at which point you can use its methods to set up your mod.
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule)
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule...)
     */
    @Deprecated
    public static void initializeMod(String modId, BalmRuntimeLoadContext context, Runnable initializer) {
        runtime.initializeMod(modId, context, (registrars) -> initializer.run());
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
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, Runnable)
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule...)
     */
    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmModule module) {
        runtime.initializeMod(modId, context, (registrars) -> registerModule(module));
    }

    /**
     * You must call this or any of its overloads in each of your mod's entry points. Provide a load context specific to each mod loader.
     * Everything else you do in Balm should happen inside the module, whose methods are called at a time that Balm has
     * initialized the runtime for your mod.
     *
     * @param modId   the mod id for your mod.
     * @param context the load context for the mod loader you are using, e.g. NeoForgeLoadContext.
     * @param modules one or more implementations of {@link BalmModule} within which you can set up your mod.
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, Runnable)
     * @see Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule)
     */
    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmModule... modules) {
        runtime.initializeMod(modId, context, (registrars) -> {
            for (final var module : modules) {
                registerModule(module);
            }
        });
    }

    /**
     * Creates a platform proxy, which you can use to provide differing implementations for each mod loader under a common interface.
     *
     * @param <T> the type of the common interface.
     * @return a builder for a platform proxy.
     */
    public static <T> PlatformProxy<T> platformProxy() {
        return runtime.platformProxy();
    }

    /**
     * Creates a mod proxy, which you can use to provide differing implementations depending on which mods are loaded under a common interface.
     *
     * @param <T> the type of the common interface.
     * @return a builder for a mod proxy.
     */
    public static <T> ModProxy<T> modProxy() {
        return runtime.modProxy();
    }

    /**
     * Creates a sided proxy, which you can use to provide differing implementations for whether the mod is running on the client or a dedicated server under a common interface.
     * When providing class names, do not use {@link Class#getName()} as that will cause the class to be loaded and cause crashes on dedicated servers!
     * Provide a literal string for the class names instead.
     * Both classes must have a public no-arg constructor.
     *
     * @param commonName the class name to load on dedicated servers.
     * @param clientName the class name to load on clients.
     * @param <T>        the type of the common interface.
     * @return a builder for a sided proxy.
     */
    public static <T> SidedProxy<T> sidedProxy(String commonName, String clientName) {
        return runtime.sidedProxy(commonName, clientName);
    }

    /**
     * Instantiates the given class if the mod id in question is loaded.
     * Do not use {@link Class#getName()} as that will cause the class to be loaded and cause crashes if the mod is not loaded!
     * Provide a literal string for the class name instead. The class must have a public no-arg constructor.
     *
     * @param modId     the mod id that needs to be loaded for the class to be instantiated.
     * @param className the name of the class to be instantiated.
     */
    public static void initializeIfLoaded(String modId, String className) {
        runtime.initializeIfLoaded(modId, className);
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#resourceReloadListeners(String, Consumer)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#resourceReloadListeners(String, Consumer)} instead.
     */
    @Deprecated
    public static void addServerReloadListener(ResourceLocation identifier, Function<HolderLookup.Provider, PreparableReloadListener> reloadListener) {
        runtime.addServerReloadListener(identifier, reloadListener);
    }

    /**
     * Provides access to common client-sided functions with no-op implementations for dedicated servers.
     * <p>
     * Needing access to client-sided functions outside a client-only context is often a sign of a design issue.
     * Consider refactoring your code to better separate client-side implementations instead.
     *
     * @return a resolved sided proxy that allows access to common client-only functions
     */
    public static BalmProxy safeClientAccess() {
        return runtime.getProxy();
    }

    public static BalmEvents events() {
        return runtime.getEvents();
    }

    /**
     * Provides access to config-related functions such as registering and retrieving configs.
     *
     * @return implementation of {@link BalmConfig} for the mod loader Balm is running on.
     */
    public static BalmConfig config() {
        return runtime.getConfig();
    }

    /**
     * Provides access to networking-related functions such as registering and sending packets.
     *
     * @return implementation of {@link BalmNetworking} for the mod loader Balm is running on.
     */
    public static BalmNetworking networking() {
        return runtime.getNetworking();
    }

    /**
     * Provides access to registering biome modifiers, allowing you to add features to existing biomes.
     *
     * @return implementation of {@link BalmWorldGen} for the mod loader Balm is running on.
     */
    public static BalmWorldGen biomeModifications() {
        return runtime.getWorldGen();
    }

    /**
     * Provides access to mod loader-specific utilities and hooks.
     *
     * @return implementation of {@link BalmHooks} for the mod loader Balm is running on.
     */
    public static BalmHooks hooks() {
        return runtime.getHooks();
    }

    /**
     * Provides access to capabilities, which are logic providers that can be attached to block entities.
     *
     * @return implementation of {@link BalmCapabilities} for the mod loader Balm is running on.
     */
    public static BalmCapabilities capabilities() {
        return runtime.getCapabilities();
    }

    /**
     * Provides access to command registration.
     *
     * @return implementation of {@link BalmCommands} for the mod loader Balm is running on.
     */
    public static BalmCommands commands() {
        return runtime.getCommands();
    }

    /**
     * Provides access to registering loot modifiers.
     *
     * @return implementation of {@link BalmLootTables} for the mod loader Balm is running on.
     */
    public static BalmLootTables lootModifiers() {
        return runtime.getLootTables();
    }

    /**
     * Provides access to mod support related functions, such as
     * <li>a universal milk fluid</li>
     * <li>accessories (Curios/Trinkets)</li>
     * <li>hud info mods (WAILA, Jade)</li>
     *
     * @return implementation of {@link BalmModSupport} for the environment Balm is running on.
     */
    public static BalmModSupport modSupport() {
        return runtime.getModSupport();
    }

    /**
     * Provides access to registering and checking for permissions.
     *
     * @return implementation of {@link BalmPermissions} for the mod loader Balm is running on.
     */
    public static BalmPermissions permissions() {
        return runtime.getPermissions();
    }

    /**
     * @deprecated Use {{@link net.blay09.mods.balm.core.BalmRegistrars#resourceConditions(String, Consumer)}} instead.
     */
    @Deprecated
    public static BalmResources resources() {
        return runtime.getResources();
    }

    /**
     * For internal use. Provides access to the runtime powering mod-loader specific functions.
     * Generally, you should not need to access the runtime directly, as all its methods are exposed on {@link Balm}.
     */
    @ApiStatus.Internal
    public static BalmRuntime<? extends BalmRuntimeLoadContext> getRuntime() {
        return runtime;
    }

    /**
     * Provides access to mod loader related functions, such as checking if a mod is loaded or accessing jar contents.
     *
     * @return implementation of {@link BalmPlatform} for the mod loader Balm is running on.
     */
    public static BalmPlatform platform() {
        return runtime.platform();
    }

    /**
     * @see Balm#permissions()
     * @deprecated Use {@link Balm#permissions()} instead.
     */
    @Deprecated
    public static BalmPermissions getPermissions() {
        return permissions();
    }

    /**
     * @see Balm#modSupport()
     * @deprecated Use {@link Balm#modSupport()} instead.
     */
    @Deprecated
    public static BalmModSupport getModSupport() {
        return modSupport();
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#registrar()
     * @see net.blay09.mods.balm.core.BalmRegistrars#registrar(ResourceKey, String)
     * @see Balm#getModSupport()
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#registrar()} instead, or {@link Balm#getModSupport()} for the milk fluid.
     */
    @Deprecated
    public static BalmRegistries getRegistries() {
        return runtime.getRegistries();
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#blockEntityTypes(String, Consumer)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#blockEntityTypes(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmBlockEntities getBlockEntities() {
        return runtime.getBlockEntities();
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#items(String, Consumer)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmItems getItems() {
        return runtime.getItems();
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmBlocks getBlocks() {
        return runtime.getBlocks();
    }

    /**
     * @see Balm#platform()
     * @see BalmPlatform#name()
     * @deprecated Use {@link Balm#platform()} and {@link BalmPlatform#name()} instead.
     */
    @Deprecated
    public static String getPlatform() {
        return platform().name();
    }

    /**
     * @see Balm#platform()
     * @see BalmPlatform#physicalSide()
     * @deprecated Use {@link Balm#platform()} and {@link BalmPlatform#physicalSide()} instead.
     */
    @Deprecated
    public static BalmEnvironment getEnvironment() {
        return platform().physicalSide();
    }

    /**
     * @see Balm#platform()
     * @see BalmPlatform#loadedPrimaryModIds()
     * @deprecated Use {@link Balm#platform()} and {@link BalmPlatform#loadedPrimaryModIds()} instead.
     */
    @Deprecated
    public static List<String> getLoadedPrimaryModIds() {
        return platform().loadedPrimaryModIds();
    }

    /**
     * @see Balm#platform()
     * @see BalmPlatform#isModLoaded(String)
     * @deprecated Use {@link Balm#platform()} and {@link BalmPlatform#isModLoaded(String)} instead.
     */
    @Deprecated
    public static boolean isModLoaded(String modId) {
        return platform().isModLoaded(modId);
    }

    /**
     * @see Balm#platform()
     * @see BalmPlatform#getModName(String)
     * @deprecated Use {@link Balm#platform()} and {@link BalmPlatform#getModName(String)} instead.
     */
    @Deprecated
    public static String getModName(String modId) {
        return platform().getModName(modId);
    }

    /**
     * @see Balm#platform()
     * @see BalmPlatform#isDevelopmentEnvironment()
     * @deprecated Use {@link Balm#platform()} and {@link BalmPlatform#isDevelopmentEnvironment()} instead.
     */
    @Deprecated
    public static boolean isDevelopmentEnvironment() {
        return platform().isDevelopmentEnvironment();
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#registrar(net.minecraft.resources.ResourceKey, String)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#registrar(net.minecraft.resources.ResourceKey, String)} instead.
     */
    @Deprecated
    public static BalmSounds getSounds() {
        return runtime.getSounds();
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#dataComponentTypes(String, Consumer)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#dataComponentTypes(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmComponents getComponents() {
        return runtime.getComponents();
    }

    /**
     * @see Balm#resources()
     * @deprecated Use {@link Balm#resources()} instead.
     */
    @Deprecated
    public static BalmResources getResources() {
        return resources();
    }

    /**
     * @see Balm#resources()
     * @see BalmPlatform#visitModResources(String, String, ModResourceVisitor)
     * @deprecated Use {@link Balm#platform()} and {@link BalmPlatform#visitModResources(String, String, ModResourceVisitor)} instead.
     */
    @Deprecated
    public static void visitModResources(String modId, String path, ModResourceVisitor visitor) {
        platform().visitModResources(modId, path, visitor);
    }

    /**
     * @see Balm#resources()
     * @see BalmPlatform#lookupModResource(String, String)
     * @deprecated Use {@link Balm#platform()} and {@link BalmPlatform#lookupModResource(String, String)} instead.
     */
    @Deprecated
    public static Optional<ModResource> lookupModResource(String modId, String path) {
        return platform().lookupModResource(modId, path);
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(String, java.util.function.Consumer)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(String, java.util.function.Consumer)} instead.
     */
    @Deprecated
    public static BalmRecipes getRecipes() {
        return runtime.getRecipes();
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#menuTypes(String, Consumer)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#menuTypes(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmMenus getMenus() {
        return runtime.getMenus();
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#resourceReloadListeners(String, Consumer)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#resourceReloadListeners(String, Consumer)} instead.
     */
    @Deprecated
    public static void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        runtime.addServerReloadListener(identifier, it -> reloadListener);
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#resourceReloadListeners(String, Consumer)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#resourceReloadListeners(String, Consumer)} instead.
     */
    @Deprecated
    public static void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        runtime.addServerReloadListener(identifier, reloadListener);
    }

    /**
     * @see net.blay09.mods.balm.core.BalmRegistrars#entityTypes(String, java.util.function.Consumer)
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#entityTypes(String, java.util.function.Consumer)} instead.
     */
    @Deprecated
    public static BalmEntities getEntities() {
        return runtime.getEntities();
    }

    /**
     * @see #config()
     * @deprecated Use {@link #config()} instead.
     */
    @Deprecated
    public static BalmConfig getConfig() {
        return config();
    }

    /**
     * @see #networking()
     * @deprecated Use {@link #networking()} instead.
     */
    @Deprecated
    public static BalmNetworking getNetworking() {
        return networking();
    }

    /**
     * @see #biomeModifications()
     * @see net.blay09.mods.balm.core.BalmRegistrars#registrar(ResourceKey, String)
     * @deprecated Use {@link #biomeModifications()} or {@link net.blay09.mods.balm.core.BalmRegistrars#registrar(ResourceKey, String)} instead.
     */
    @Deprecated
    public static BalmWorldGen getWorldGen() {
        return biomeModifications();
    }

    /**
     * @see #events()
     * @deprecated Use {@link #events()} instead.
     */
    @Deprecated
    public static BalmEvents getEvents() {
        return events();
    }

    /**
     * @see #safeClientAccess()
     * @deprecated Use {@link #safeClientAccess()} instead.
     */
    @Deprecated
    public static BalmProxy getProxy() {
        return runtime.getProxy();
    }

    /**
     * @deprecated Use {@link #lootModifiers()} instead.
     */
    @Deprecated
    public static BalmLootTables getLootTables() {
        return lootModifiers();
    }

    /**
     * @deprecated Use {@link #commands()} instead.
     */
    @Deprecated
    public static BalmCommands getCommands() {
        return commands();
    }

    /**
     * @deprecated Use {@link #capabilities()} instead.
     */
    @Deprecated
    public static BalmCapabilities getCapabilities() {
        return capabilities();
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#particleTypes(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmParticles getParticles() {
        return runtime.getParticles();
    }

    /**
     * @deprecated Use {@link #hooks()} instead.
     */
    @Deprecated
    public static BalmHooks getHooks() {
        return hooks();
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#customStats(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmStats getStats() {
        return runtime.getStats();
    }
}
