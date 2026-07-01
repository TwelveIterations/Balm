package net.blay09.mods.balm;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.client.platform.module.BalmClientModule;
import net.blay09.mods.balm.platform.capabilities.BalmCapabilities;
import net.blay09.mods.balm.commands.BalmCommands;
import net.blay09.mods.balm.platform.compatibility.BalmModSupport;
import net.blay09.mods.balm.platform.config.BalmConfig;
import net.blay09.mods.balm.world.level.storage.loot.BalmLootTables;
import net.blay09.mods.balm.platform.module.BalmModule;
import net.blay09.mods.balm.network.BalmNetworking;
import net.blay09.mods.balm.platform.permissions.BalmPermissions;
import net.blay09.mods.balm.platform.ModProxy;
import net.blay09.mods.balm.platform.PlatformProxy;
import net.blay09.mods.balm.platform.SidedProxy;
import net.blay09.mods.balm.world.level.levelgen.BalmWorldGen;
import net.blay09.mods.balm.core.BalmRegistrars;
import net.blay09.mods.balm.platform.BalmHooks;
import net.blay09.mods.balm.platform.BalmPlatform;
import net.blay09.mods.balm.platform.BalmSafeClientAccess;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntime;
import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntimeSpi;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

/**
 * Provides access to common registry functions as well as various loader-specific utilities.
 * <p>
 * To initialize your mod with Balm, use {@link #initializeMod(String, BalmRuntimeLoadContext, BalmModule)} or its overloads,
 * passing either an implementation of {@link BalmModule} or a {@link Runnable}.
 * <p>
 * If you are using client-side features of Balm, you must also initialize the client-side runtime
 * using {@link BalmClient#initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)} or its overloads.
 *
 * @see BalmClient
 */
public class Balm {
    private static final BalmRuntime<BalmRuntimeLoadContext> runtime = BalmRuntimeSpi.create();

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
     * Creates a mod proxy, which you can use to provide differing implementations depending on which mods are loaded under a common interface.
     *
     * @param identifier the identifier for this mod proxy.
     * @param <T>        the type of the common interface.
     * @return a builder for a mod proxy.
     */
    public static <T> ModProxy<T> modProxy(Identifier identifier) {
        return runtime.modProxy(identifier);
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
     * Provides access to common client-sided functions with no-op implementations for dedicated servers.
     * <p>
     * Needing access to client-sided functions outside a client-only context is often a sign of a design issue.
     * Consider refactoring your code to better separate client-side implementations instead.
     *
     * @return a resolved sided proxy that allows access to common client-only functions
     */
    public static BalmSafeClientAccess safeClientAccess() {
        return runtime.getProxy();
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
     * For internal use. Provides access to the runtime powering mod-loader specific functions.
     * Generally, you should not need to access the runtime directly, as all its methods are exposed on {@link Balm}.
     */
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

}
