package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.keymappings.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

public class BalmClient {
    private static final BalmClientRuntime<BalmRuntimeLoadContext> runtime = BalmClientRuntimeSpi.create();

    /**
     * Not to be confused with {@link #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)}, which should be used
     * for registering your mod with Balm. This method registers an additional module and should only be called from an
     * initializer or entrypoint. Some things may not work as expected if you try to register a module before
     * <code>initializeMod</code> has been called.
     *
     * @param module the module to register for an already initialized mod.
     * @see #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule)
     */
    public static void registerModule(BalmClientModule module) {
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
     * @see #initializeMod(String, BalmRuntimeLoadContext, Runnable)
     * @see #initializeMod(String, BalmRuntimeLoadContext, BalmClientModule...)
     */
    public static <T extends BalmRuntimeLoadContext> void initializeMod(String modId, T context, BalmClientModule module) {
        runtime.initializeMod(modId, context, () -> registerModule(module));
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
        runtime.initializeMod(modId, context, () -> {
            for (final var module : modules) {
                registerModule(module);
            }
        });
    }

    /**
     * Use this to register menu screens using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id to register menu types under.
     * @param initializer Callback that receives a scoped registrar for registering menu types.
     */
    public static void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer) {
        runtime.menuScreens(namespace, initializer);
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
     * Use this to register block entity renderers using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id to register block entity types under.
     * @param initializer Callback that receives a scoped registrar for registering block entity types.
     */
    public static void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer) {
        runtime.blockEntityRenderers(namespace, initializer);
    }

    /**
     * Use this to register entity renderers using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id to register entity types under.
     * @param initializer Callback that receives a scoped registrar for registering entity types.
     */
    public static void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer) {
        runtime.entityRenderers(namespace, initializer);
    }

    /**
     * Use this to register models using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id under which models should be registered.
     * @param initializer Callback that receives a scoped registrar for models.
     */
    public static void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer) {
        runtime.blockStateModels(namespace, initializer);
    }

    /**
     * Use this to register key mappings using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id under which key mappings should be registered.
     * @param initializer Callback that receives a scoped registrar for key mappings.
     */
    public static void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer) {
        runtime.keyMappings(namespace, initializer);
    }

    public static void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        runtime.addResourceReloadListener(identifier, reloadListener);
    }

    public static BalmRenderers getRenderers() {
        return runtime.getRenderers();
    }

    /**
     * @deprecated Use {@link #keyMappings(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmKeyMappings getKeyMappings() {
        return runtime.getKeyMappings();
    }

    /**
     * @deprecated Use {@link #blockStateModels(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmModels getModels() {
        return runtime.getModels();
    }

    /**
     * @deprecated Use {@link #menuScreens(String, Consumer)} instead.
     */
    @Deprecated
    public static BalmScreens getScreens() {
        return runtime.getScreens();
    }
}
