package net.blay09.mods.balm.client;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;

import java.util.function.Consumer;

/**
 * An instance of this class is passed to your initializer when using {@link net.blay09.mods.balm.api.client.BalmClient#initializeMod(String, BalmRuntimeLoadContext, Consumer)}.
 * <p>
 * If you are using {@link net.blay09.mods.balm.api.client.module.BalmClientModule}, you do not need to use this class as BalmClientModule comes with <code>register{...}</code> methods that are called with each registrar automatically.
 */
public class BalmClientRegistrars {

    private final BalmClientRuntime<?> runtime;

    public BalmClientRegistrars(BalmClientRuntime<?> runtime) {
        this.runtime = runtime;
    }

    /**
     * Use this to register menu screens using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id to register menu types under.
     * @param initializer Callback that receives a scoped registrar for registering menu types.
     */
    public void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer) {
        runtime.menuScreens(namespace, initializer);
    }

    /**
     * Use this to register block entity renderers using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id to register block entity types under.
     * @param initializer Callback that receives a scoped registrar for registering block entity types.
     */
    public void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer) {
        runtime.blockEntityRenderers(namespace, initializer);
    }

    /**
     * Use this to register entity renderers using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id to register entity types under.
     * @param initializer Callback that receives a scoped registrar for registering entity types.
     */
    public void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer) {
        runtime.entityRenderers(namespace, initializer);
    }

    /**
     * Use this to register models using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id under which models should be registered.
     * @param initializer Callback that receives a scoped registrar for models.
     */
    public void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer) {
        runtime.blockStateModels(namespace, initializer);
    }

    /**
     * Use this to register model layers using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id under which model layers should be registered.
     * @param initializer Callback that receives a scoped registrar for model layers.
     */
    public void modelLayers(String namespace, Consumer<BalmModelLayerRegistrar> initializer) {
        runtime.modelLayers(namespace, initializer);
    }

    /**
     * Use this to register block color handlers using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id under which block colors should be registered.
     * @param initializer Callback that receives a scoped registrar for block colors.
     */
    public void blockColors(String namespace, Consumer<BalmBlockColorRegistrar> initializer) {
        runtime.blockColors(namespace, initializer);
    }

    /**
     * Use this to register particle providers using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id under which particle providers should be registered.
     * @param initializer Callback that receives a scoped registrar for particle providers.
     */
    public void particleProviders(String namespace, Consumer<BalmParticleProviderRegistrar> initializer) {
        runtime.particleProviders(namespace, initializer);
    }

    /**
     * Use this to register block render types using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id under which block render types should be registered.
     * @param initializer Callback that receives a scoped registrar for block render types.
     */
    public void blockRenderTypes(String namespace, Consumer<BalmBlockRenderTypeRegistrar> initializer) {
        runtime.blockRenderTypes(namespace, initializer);
    }

    /**
     * Use this to register key mappings using the registrar provided in the consumer callback.
     *
     * @param namespace   The mod id under which key mappings should be registered.
     * @param initializer Callback that receives a scoped registrar for key mappings.
     */
    public void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer) {
        runtime.keyMappings(namespace, initializer);
    }

    /**
     * Use this to register resource reload listeners using the registrar provided in the consumer callback.
     *
     * @param namespace The mod id under which reload listeners should be registered.
     * @param initializer Callback that receives a scoped registrar for reload listeners.
     */
    public void resourceReloadListeners(String namespace, Consumer<BalmClientResourceReloadListenerRegistrar> initializer) {
        runtime.resourceReloadListeners(namespace, initializer);
    }
}
