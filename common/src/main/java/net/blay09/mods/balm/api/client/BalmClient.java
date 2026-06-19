package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.commands.BalmClientCommands;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Consumer;

public class BalmClient {
    private static final BalmClientRuntime<BalmRuntimeLoadContext> runtime = BalmClientRuntimeSpi.create();

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#registerModule(BalmClientModule)} instead.
     */
    @Deprecated
    public static void registerModule(BalmClientModule module) {
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
    public static <T extends BalmRuntimeLoadContext> void initialize(String modId, T context, Runnable initializer) {
        initializeMod(modId, context, initializer);
    }

    /**
     * @deprecated Use {@link #initializeMod(String, BalmRuntimeLoadContext, Consumer)} instead.
     */
    @Deprecated
    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, Runnable initializer) {
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
    public static void initializeMod(String modId, BalmRuntimeLoadContext context, Consumer<BalmClientRegistrars> initializer) {
        runtime.initializeMod(modId, context, initializer);
    }

    public static BalmRenderers getRenderers() {
        return runtime.getRenderers();
    }

    /**
     * @deprecated Use {@link net.blay09.mods.kuma.api.Kuma} or {@link BalmClientRegistrars#keyMappings(Consumer)} instead.
     */
    @Deprecated
    public static BalmKeyMappings getKeyMappings() {
        return runtime.getKeyMappings();
    }

    /**
     * @deprecated Use {@link BalmClientRegistrars#menuScreens(Consumer)} instead.
     */
    @Deprecated
    public static BalmScreens getScreens() {
        return runtime.getScreens();
    }

    public static BalmModels getModels() {
        return runtime.getModels();
    }

    /**
     * For internal use. Provides access to the runtime powering mod-loader specific functions.
     * Generally, you should not need to access the runtime directly, as all its methods are exposed on {@link BalmClient}.
     */
    public static BalmClientRuntime<? extends BalmRuntimeLoadContext> getRuntime() {
        return runtime;
    }

    /**
     * @deprecated Use {@link BalmClientRegistrars#resourceReloadListeners(Consumer)} instead.
     */
    @Deprecated
    public static void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        runtime.addResourceReloadListener(identifier, reloadListener);
    }

    /**
     * @deprecated No functionality left here.
     */
    @Deprecated
    public static BalmTextures getTextures() {
        return runtime.getTextures();
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
