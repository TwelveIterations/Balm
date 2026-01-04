package net.blay09.mods.balm.client;

import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.client.platform.runtime.internal.BalmClientRuntime;
import net.blay09.mods.balm.client.platform.module.BalmClientModule;
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
 * An instance of this class is passed to your initializer when using {@link BalmClient#initializeMod(String, BalmRuntimeLoadContext, Consumer)}.
 * <p>
 * If you are using {@link BalmClientModule}, you do not need to use this class as BalmClientModule comes with <code>register{...}</code> methods that are called with each registrar automatically.
 */
public class BalmClientRegistrars {

    private final BalmClientRuntime<?> runtime;
    private final String namespace;

    public BalmClientRegistrars(BalmClientRuntime<?> runtime, String namespace) {
        this.runtime = runtime;
        this.namespace = namespace;
    }

    /**
     * Use this to register menu screens using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering menu types.
     */
    public void menuScreens(Consumer<BalmMenuScreenRegistrar> initializer) {
        runtime.menuScreens(namespace, initializer);
    }

    /**
     * Use this to register block entity renderers using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering block entity types.
     */
    public void blockEntityRenderers(Consumer<BalmBlockEntityRendererRegistrar> initializer) {
        runtime.blockEntityRenderers(namespace, initializer);
    }

    /**
     * Use this to register entity renderers using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering entity types.
     */
    public void entityRenderers(Consumer<BalmEntityRendererRegistrar> initializer) {
        runtime.entityRenderers(namespace, initializer);
    }

    /**
     * Use this to register models using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for models.
     */
    public void blockStateModels(Consumer<BalmBlockStateModelRegistrar> initializer) {
        runtime.blockStateModels(namespace, initializer);
    }

    /**
     * Use this to register model layers using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for model layers.
     */
    public void modelLayers(Consumer<BalmModelLayerRegistrar> initializer) {
        runtime.modelLayers(namespace, initializer);
    }

    /**
     * Use this to register block color handlers using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for block colors.
     */
    public void blockColors(Consumer<BalmBlockColorRegistrar> initializer) {
        runtime.blockColors(namespace, initializer);
    }

    /**
     * Use this to register particle providers using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for particle providers.
     */
    public void particleProviders(Consumer<BalmParticleProviderRegistrar> initializer) {
        runtime.particleProviders(namespace, initializer);
    }

    /**
     * Use this to register block render types using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for block render types.
     */
    public void blockRenderTypes(Consumer<BalmBlockRenderTypeRegistrar> initializer) {
        runtime.blockRenderTypes(namespace, initializer);
    }

    /**
     * Use this to register key mappings using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for key mappings.
     */
    public void keyMappings(Consumer<BalmKeyMappingRegistrar> initializer) {
        runtime.keyMappings(namespace, initializer);
    }

    /**
     * Use this to register resource reload listeners using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for reload listeners.
     */
    public void resourceReloadListeners(Consumer<BalmClientResourceReloadListenerRegistrar> initializer) {
        runtime.resourceReloadListeners(namespace, initializer);
    }

    /**
     * Use this to register tooltip components using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for tooltip components.
     */
    public void clientTooltipComponents(Consumer<BalmClientTooltipComponentRegistrar> initializer) {
        runtime.clientTooltipComponents(namespace, initializer);
    }

    /**
     * Use this to register range select item model properties using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for range select item model properties.
     */
    public void rangeSelectItemModelProperties(Consumer<BalmRangeSelectItemModelPropertyRegistrar> initializer) {
        runtime.rangeSelectItemModelProperties(namespace, initializer);
    }

    public void registerModule(BalmClientModule module) {
        runtime.registerModule(this, module);
    }
}
