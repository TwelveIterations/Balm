package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.api.module.BalmModule;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * Provides access to common registry functions as well as various loader-specific utilities.
 * <p>
 * To initialize your mod with Balm, use {@link #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)} or its overloads,
 * passing either an implementation of {@link BalmClientModule} or a {@link Runnable}.
 * <p>
 * You must also initialize the common runtime using {@link net.blay09.mods.balm.api.Balm#initializeMod(String, BalmRuntimeLoadContext, BalmModule)} or its overloads.
 *
 * @see net.blay09.mods.balm.api.Balm
 */
public class BalmClient {
    private static final BalmClientRuntime<BalmRuntimeLoadContext> runtime = BalmClientRuntimeSpi.create();

    /**
     * Not to be confused with {@link #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)}, which should be used
     * for registering your mod with Balm. This method registers an additional module and should only be called from an
     * initializer or entrypoint. Some things may not work as expected if you try to register a module before
     * <code>initializeMod</code> has been called.
     *
     * @deprecated Use {@link BalmClientRegistrars#registerModule(BalmClientModule)} instead.
     * @param module the module to register for an already initialized mod.
     * @see #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)
     */
    @Deprecated
    public static void registerModule(BalmClientModule module) {
        runtime.registerModule(module);
    }

    /**
     * You must call this or any of its overloads in each of your mod's client entry points. Provide a load context specific to each mod loader.
     * Everything else you do in Balm should happen inside the initializer, which runs at a time that Balm has
     * initialized the runtime for your mod.
     *
     * @deprecated Use the variant that takes a {@link Consumer} instead, giving you access to {@link net.blay09.mods.balm.client.BalmClientRegistrars}.
     * @param modId       The mod id for your mod.
     * @param context     The load context for the mod loader you are using, e.g. NeoForgeLoadContext.
     * @param initializer Callback that runs when Balm is ready, at which point you can use its methods to set up your mod.
     * @see #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)
     * @see #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule...)
     */
    @Deprecated
    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, Runnable initializer) {
        runtime.initializeMod(modId, context, initializer);
    }

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
     * @see #initializeMod(String, BalmRuntimeLoadContext, Runnable)
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
     * @see #initializeMod(String, BalmRuntimeLoadContext, Runnable)
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
    @ApiStatus.Internal
    public static BalmClientRuntime<? extends BalmRuntimeLoadContext> getRuntime() {
        return runtime;
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#resourceReloadListeners(String, java.util.function.Consumer)} instead.
     */
    @Deprecated
    public static void addResourceReloadListener(Identifier identifier, PreparableReloadListener reloadListener) {
        runtime.addResourceReloadListener(identifier, reloadListener);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#blockRenderTypes(String, Consumer)}, {@link net.blay09.mods.balm.client.BalmClientRegistrars#blockStateModels(String, Consumer)}, {@link net.blay09.mods.balm.client.BalmClientRegistrars#modelLayers(String, Consumer)}, {@link net.blay09.mods.balm.client.BalmClientRegistrars#blockColors(String, Consumer)}, {@link net.blay09.mods.balm.client.BalmClientRegistrars#entityRenderers(String, Consumer)}, {@link net.blay09.mods.balm.client.BalmClientRegistrars#blockEntityRenderers(String, Consumer)} or {@link net.blay09.mods.balm.client.BalmClientRegistrars#particleProviders(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmRenderers getRenderers() {
        return runtime.getRenderers();
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#keyMappings(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmKeyMappings getKeyMappings() {
        return runtime.getKeyMappings();
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#blockStateModels(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmModels getModels() {
        return runtime.getModels();
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#menuScreens(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmScreens getScreens() {
        return runtime.getScreens();
    }
}
