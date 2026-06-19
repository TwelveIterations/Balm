package net.blay09.mods.balm.client;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.commands.BalmClientCommands;
import net.blay09.mods.balm.client.platform.BalmClientHooks;
import net.blay09.mods.balm.client.platform.runtime.internal.BalmClientRuntime;
import net.blay09.mods.balm.client.platform.runtime.internal.BalmClientRuntimeSpi;
import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.client.platform.module.BalmClientModule;
import net.blay09.mods.balm.platform.module.BalmModule;

import java.util.function.Consumer;

/**
 * Provides access to common registry functions as well as various loader-specific utilities.
 * <p>
 * To initialize your mod with Balm, use {@link #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)} or its overloads,
 * passing either an implementation of {@link BalmClientModule} or a {@link Runnable}.
 * <p>
 * You must also initialize the common runtime using {@link Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule)} or its overloads.
 *
 * @see Balm
 */
public class BalmClient {
    private static final BalmClientRuntime<BalmRuntimeLoadContext> runtime = BalmClientRuntimeSpi.create();

    /**
     * You must call this or any of its overloads in each of your mod's client entry points. Provide a load context specific to each mod loader.
     * Everything else you do in Balm should happen inside the initializer, which runs at a time that Balm has
     * initialized the runtime for your mod.
     *
     * @param modId       The mod id for your mod.
     * @param context     The load context for the mod loader you are using, e.g. NeoForgeLoadContext.
     * @param initializer Callback that runs when Balm is ready, at which point you can use its methods to set up your mod.
     * @see #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)
     * @see #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule...)
     */
    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, Consumer<BalmClientRegistrars> initializer) {
        runtime.initializeMod(modId, context, initializer);
    }

    /**
     * You must call this or any of its overloads in each of your mod's client entry points. Provide a load context specific to each mod loader.
     * Everything else you do in Balm should happen inside the module, whose methods are called at a time that Balm has
     * initialized the runtime for your mod.
     *
     * @param modId   the mod id for your mod.
     * @param context the load context for the mod loader you are using, e.g. NeoForgeLoadContext.
     * @param module  an implementation of {@link BalmClientModule} within which you can set up your mod.
     * @see #initializeMod(String, BalmRuntimeLoadContext, Consumer)
     * @see #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule...)
     */
    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmClientModule module) {
        runtime.initializeMod(modId, context, (registrars) -> registrars.registerModule(module));
    }

    /**
     * You must call this or any of its overloads in each of your mod's client entry points. Provide a load context specific to each mod loader.
     * Everything else you do in Balm should happen inside the module, whose methods are called at a time that Balm has
     * initialized the runtime for your mod.
     *
     * @param modId   the mod id for your mod.
     * @param context the load context for the mod loader you are using, e.g. NeoForgeLoadContext.
     * @param modules one or more implementations of {@link BalmClientModule} within which you can set up your mod.
     * @see #initializeMod(String, BalmRuntimeLoadContext, Consumer)
     * @see #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)
     */
    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmClientModule... modules) {
        runtime.initializeMod(modId, context, (registrars) -> {
            for (final var module : modules) {
                registrars.registerModule(module);
            }
        });
    }

    /**
     * For internal use. Provides access to the runtime powering mod-loader specific functions.
     * Generally, you should not need to access the runtime directly, as all its methods are exposed on {@link Balm}.
     */
    public static BalmClientRuntime<? extends BalmRuntimeLoadContext> getRuntime() {
        return runtime;
    }

    /**
     * Provides access to client-side mod loader-specific utilities and hooks.
     *
     * @return implementation of {@link BalmClientHooks} for the mod loader Balm is running on.
     */
    public static BalmClientHooks clientHooks() {
        return runtime.clientHooks();
    }

    /**
     * Provides access to client command registration.
     *
     * @return implementation of {@link BalmClientCommands} for the mod loader Balm is running on.
     */
    public static BalmClientCommands clientCommands() {
        return runtime.clientCommands();
    }

}
