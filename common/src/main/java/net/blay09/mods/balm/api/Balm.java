package net.blay09.mods.balm.api;

import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.command.BalmCommands;
import net.blay09.mods.balm.api.compat.BalmModSupport;
import net.blay09.mods.balm.api.component.BalmComponents;
import net.blay09.mods.balm.api.config.BalmConfig;
import net.blay09.mods.balm.api.entity.BalmEntities;
import net.blay09.mods.balm.api.entity.BalmEntityTypeFactory;
import net.blay09.mods.balm.api.event.BalmEvents;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.api.loot.BalmLootTables;
import net.blay09.mods.balm.api.menu.BalmMenus;
import net.blay09.mods.balm.api.menu.BalmMenuTypeFactory;
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
import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.loader.BalmPlatform;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabFactory;
import net.blay09.mods.balm.world.item.BalmItemFactory;
import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeFactory;
import net.blay09.mods.balm.world.component.BalmDataComponentTypeFactory;
import net.blay09.mods.balm.world.level.block.BalmBlockFactory;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeFactory;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Optional;
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
    public static void initializeMod(String modId, BalmRuntimeLoadContext context, Runnable initializer) {
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
        runtime.initializeMod(modId, context, () -> registerModule(module));
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
        runtime.initializeMod(modId, context, () -> {
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
     * @deprecated Use {@link #resources()} and {@link BalmResources#addServerReloadListener(ResourceLocation, Function)} instead.
     * @see #resources()
     * @see BalmResources#addServerReloadListener(ResourceLocation, Function)
     */
    @Deprecated
    public static void addServerReloadListener(ResourceLocation identifier, Function<HolderLookup.Provider, PreparableReloadListener> reloadListener) {
        resources().addServerReloadListener(identifier, reloadListener);
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

    /**
     * Use this to register menu types using the factory provided in the consumer callback.
     *
     * @param namespace   The mod id to register menu types under.
     * @param initializer Callback that receives a scoped factory for registering menu types.
     */
    public static void menuTypes(String namespace, java.util.function.Consumer<BalmMenuTypeFactory> initializer) {
        runtime.menuTypes(namespace, initializer);
    }

    /**
     * Use this to register entity types using the factory provided in the consumer callback.
     *
     * @param namespace   The mod id to register entity types under.
     * @param initializer Callback that receives a scoped factory for registering entity types.
     */
    public static void entityTypes(String namespace, java.util.function.Consumer<BalmEntityTypeFactory> initializer) {
        runtime.entityTypes(namespace, initializer);
    }

    public static BalmHooks getHooks() {
        return runtime.getHooks();
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

    public static BalmParticles getParticles() {
        return runtime.getParticles();
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
     * Provides access to mod jar file contents, resource load conditions, and resource reload listeners.
     *
     * @return implementation of {@link BalmResources} for the mod loader Balm is running on.
     */
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
     * Use this to register blocks using the factory provided in the consumer callback.
     *
     * @param namespace   The mod id to register blocks under.
     * @param initializer Callback that receives a scoped factory for registering blocks.
     */
    public static void blocks(String namespace, Consumer<BalmBlockFactory> initializer) {
        runtime.blocks(namespace, initializer);
    }

    /**
     * Use this to register items using the factory provided in the consumer callback.
     *
     * @param namespace   The mod id to register items under.
     * @param initializer Callback that receives a scoped factory for registering items.
     */
    public static void items(String namespace, Consumer<BalmItemFactory> initializer) {
        runtime.items(namespace, initializer);
    }

    /**
     * Use this to register recipe types and related objects using the factory provided in the consumer callback.
     *
     * @param namespace   The mod id to register recipe types under.
     * @param initializer Callback that receives a scoped factory for registering recipe types.
     */
    public static void recipeTypes(String namespace, Consumer<BalmRecipeTypeFactory> initializer) {
        runtime.recipeTypes(namespace, initializer);
    }

    /**
     * Use this to register data component types using the factory provided in the consumer callback.
     *
     * @param namespace   The mod id to register data component types under.
     * @param initializer Callback that receives a scoped factory for registering data component types.
     */
    public static void dataComponentTypes(String namespace, Consumer<BalmDataComponentTypeFactory> initializer) {
        runtime.dataComponentTypes(namespace, initializer);
    }

    /**
     * Use this to register creative mode tabs using the factory provided in the consumer callback.
     *
     * @param namespace   The mod id to register creative mode tabs under.
     * @param initializer Callback that receives a scoped factory for registering creative mode tabs.
     */
    public static void creativeModeTabs(String namespace, Consumer<BalmCreativeModeTabFactory> initializer) {
        runtime.creativeModeTabs(namespace, initializer);
    }

    /**
     * Use this to register block entity types using the factory provided in the consumer callback.
     *
     * @param namespace   The mod id to register block entity types under.
     * @param initializer Callback that receives a scoped factory for registering block entity types.
     */
    public static void blockEntityTypes(String namespace, Consumer<BalmBlockEntityTypeFactory> initializer) {
        runtime.blockEntityTypes(namespace, initializer);
    }

    /**
     * Provides a generic registrar that can be used to register entries to any registry. Consider using a scoped registar instead.
     *
     * @return a {@link BalmRegistrar} that can be used to register entries to any registry.
     * @see Balm#registrar(ResourceKey, String)
     */
    public static BalmRegistrar registrar() {
        return runtime.registrar();
    }

    /**
     * Use this to register registry objects that are not covered by the convenient factories.
     * Creates a scoped registrar for a specific registry and namespace.
     *
     * @param registryKey The {@link net.minecraft.core.registries.Registries} registry resource key.
     * @param namespace   The mod id to register entries under.
     * @param <T>         The type of the registry entries, e.g. {@link net.minecraft.sounds.SoundEvent}.
     * @return a scoped {@link BalmRegistrar} that can be used to register entries to this registry.
     * @see Balm#blocks(String, Consumer)
     * @see Balm#blockEntityTypes(String, Consumer)
     * @see Balm#items(String, Consumer)
     * @see Balm#creativeModeTabs(String, Consumer)
     * @see Balm#recipeTypes(String, Consumer)
     */
    public static <T> BalmRegistrar.Scoped<T> registrar(ResourceKey<? extends Registry<T>> registryKey, String namespace) {
        return runtime.registrar(registryKey, namespace);
    }

    /**
     * Provides access to mod loader related functions, such as checking if a mod is loaded.
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
     * @see Balm#registrar()
     * @see Balm#registrar(ResourceKey, String)
     * @see Balm#getModSupport()
     * @deprecated Use {@link Balm#registrar()} instead, or {@link Balm#getModSupport()} for the milk fluid.
     */
    @Deprecated
    public static BalmRegistries getRegistries() {
        return runtime.getRegistries();
    }

    /**
     * @see Balm#blockEntityTypes(String, Consumer)
     * @deprecated Use {@link Balm#blockEntityTypes(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmBlockEntities getBlockEntities() {
        return runtime.getBlockEntities();
    }

    /**
     * @see Balm#items(String, Consumer)
     * @deprecated Use {@link Balm#items(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmItems getItems() {
        return runtime.getItems();
    }

    /**
     * @see Balm#blocks(String, Consumer)
     * @deprecated Use {@link Balm#blocks(String, Consumer)} instead.
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
     * @see Balm#registrar(net.minecraft.resources.ResourceKey, String)
     * @deprecated Use {@link Balm#registrar(net.minecraft.resources.ResourceKey, String)} instead.
     */
    @Deprecated
    public static BalmSounds getSounds() {
        return runtime.getSounds();
    }

    /**
     * @see Balm#dataComponentTypes(String, Consumer)
     * @deprecated Use {@link Balm#dataComponentTypes(String, Consumer)} instead.
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
     * @see BalmResources#visitModResources(String, String, ModResourceVisitor)
     * @deprecated Use {@link Balm#resources()} and {@link BalmResources#visitModResources(String, String, ModResourceVisitor)} instead.
     */
    @Deprecated
    public static void visitModResources(String modId, String path, ModResourceVisitor visitor) {
        resources().visitModResources(modId, path, visitor);
    }

    /**
     * @see Balm#resources()
     * @see BalmResources#lookupModResource(String, String)
     * @deprecated Use {@link Balm#resources()} and {@link BalmResources#lookupModResource(String, String)} instead.
     */
    @Deprecated
    public static Optional<ModResource> lookupModResource(String modId, String path) {
        return resources().lookupModResource(modId, path);
    }

    /**
     * @see #recipeTypes(String, java.util.function.Consumer)
     * @deprecated Use {@link #recipeTypes(String, java.util.function.Consumer)} instead.
     */
    @Deprecated
    public static BalmRecipes getRecipes() {
        return runtime.getRecipes();
    }

    /**
     * @see #menuTypes(String, Consumer)
     * @deprecated Use {@link #menuTypes(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmMenus getMenus() {
        return runtime.getMenus();
    }

    /**
     * @deprecated Use {@link #resources()} and {@link BalmResources#addServerReloadListener(ResourceLocation, Function)} instead.
     * @see #resources()
     * @see BalmResources#addServerReloadListener(ResourceLocation, Function)
     */
    @Deprecated
    public static void addServerReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        resources().addServerReloadListener(identifier, it -> reloadListener);
    }

    /**
     * @deprecated Use {@link #resources()} and {@link BalmResources#addServerReloadListener(ResourceLocation, Function)} instead.
     * @see #resources()
     * @see BalmResources#addServerReloadListener(ResourceLocation, Function)
     */
    @Deprecated
    public static void addServerReloadListener(ResourceLocation identifier, Consumer<ResourceManager> reloadListener) {
        resources().addServerReloadListener(identifier, reloadListener);
    }
}
