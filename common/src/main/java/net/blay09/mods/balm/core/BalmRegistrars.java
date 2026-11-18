package net.blay09.mods.balm.core;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.runtime.internal.BalmRuntime;
import net.blay09.mods.balm.platform.runtime.BalmRuntimeLoadContext;
import net.blay09.mods.balm.platform.module.BalmModule;
import net.blay09.mods.balm.core.component.BalmDataComponentTypeRegistrar;
import net.blay09.mods.balm.platform.attachment.BalmDataAttachmentTypeRegistrar;
import net.blay09.mods.balm.core.particles.BalmParticleTypeRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceConditionRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmResourceReloadListenerRegistrar;
import net.blay09.mods.balm.stats.BalmCustomStatRegistrar;
import net.blay09.mods.balm.world.entity.BalmEntityTypeRegistrar;
import net.blay09.mods.balm.world.entity.ai.village.poi.BalmPoiTypeRegistrar;
import net.blay09.mods.balm.world.inventory.BalmMenuTypeRegistrar;
import net.blay09.mods.balm.world.item.BalmCreativeModeTabRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.crafting.BalmRecipeTypeRegistrar;
import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Consumer;

/**
 * An instance of this class is passed to your initializer when using {@link Balm#initializeMod(String, BalmRuntimeLoadContext, Consumer)}.
 * <p>
 * If you are using {@link BalmModule}, you do not need to use this class as BalmModule comes with <code>register{...}</code> methods that are called with each registrar automatically.
 */
public class BalmRegistrars {

    private final BalmRuntime<?> runtime;
    private final String namespace;

    public BalmRegistrars(BalmRuntime<?> runtime, String namespace) {
        this.namespace = namespace;
        this.runtime = runtime;
    }

    /**
     * Use this to register menu types using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering menu types.
     */
    public void menuTypes(Consumer<BalmMenuTypeRegistrar> initializer) {
        runtime.menuTypes(namespace, initializer);
    }

    /**
     * Use this to register entity types using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering entity types.
     */
    public void entityTypes(Consumer<BalmEntityTypeRegistrar> initializer) {
        runtime.entityTypes(namespace, initializer);
    }

    /**
     * Use this to register particle types using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering particle types.
     */
    public void particleTypes(Consumer<BalmParticleTypeRegistrar> initializer) {
        runtime.particleTypes(namespace, initializer);
    }

    /**
     * Use this to register custom stats using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering custom stats.
     */
    public void customStats(Consumer<BalmCustomStatRegistrar> initializer) {
        runtime.customStats(namespace, initializer);
    }

    /**
     * Provides a scoped registrar to register server resource reload listeners under your mod namespace.
     *
     * @param initializer Callback that receives a scoped registrar for server reload listeners.
     */
    public void resourceReloadListeners(Consumer<BalmResourceReloadListenerRegistrar> initializer) {
        runtime.resourceReloadListeners(namespace, initializer);
    }

    /**
     * Provides a registrar for registering resource conditions in a platform-agnostic way.
     *
     * @param initializer Callback receiving the resource condition registrar.
     */
    public void resourceConditions(Consumer<BalmResourceConditionRegistrar> initializer) {
        runtime.resourceConditions(namespace, initializer);
    }

    /**
     * Use this to register items using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering items.
     */
    public void items(Consumer<BalmItemRegistrar> initializer) {
        runtime.items(namespace, initializer);
    }

    /**
     * Use this to register recipe types and related objects using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering recipe types.
     */
    public void recipeTypes(Consumer<BalmRecipeTypeRegistrar> initializer) {
        runtime.recipeTypes(namespace, initializer);
    }

    /**
     * Use this to register data component types using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering data component types.
     */
    public void dataComponentTypes(Consumer<BalmDataComponentTypeRegistrar> initializer) {
        runtime.dataComponentTypes(namespace, initializer);
    }

    /**
     * Use this to register data attachment types using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering data attachment types.
     */
    public void dataAttachmentTypes(Consumer<BalmDataAttachmentTypeRegistrar> initializer) {
        runtime.dataAttachmentTypes(namespace, initializer);
    }

    /**
     * Use this to register creative mode tabs using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering creative mode tabs.
     */
    public void creativeModeTabs(Consumer<BalmCreativeModeTabRegistrar> initializer) {
        runtime.creativeModeTabs(namespace, initializer);
    }

    /**
     * Use this to register blocks using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering blocks.
     */
    public void blocks(Consumer<BalmBlockRegistrar> initializer) {
        runtime.blocks(namespace, initializer);
    }

    /**
     * Use this to register block entity types using the registrar provided in the consumer callback.
     *
     * @param initializer Callback that receives a scoped registrar for registering block entity types.
     */
    public void blockEntityTypes(Consumer<BalmBlockEntityTypeRegistrar> initializer) {
        runtime.blockEntityTypes(namespace, initializer);
    }

    public void poiTypes(Consumer<BalmPoiTypeRegistrar> initializer) {
        runtime.poiTypes(namespace, initializer);
    }

    /**
     * Provides a generic registrar that can be used to register entries to any registry. Consider using a scoped registrar instead.
     *
     * @return a {@link BalmRegistrar} that can be used to register entries to any registry.
     * @see BalmRegistrars#registrar(ResourceKey, Consumer)
     */
    public BalmRegistrar registrar() {
        return runtime.registrar();
    }

    /**
     * Use this to register registry objects that are not covered by the convenient factories.
     * Creates a scoped registrar for a specific registry and namespace.
     *
     * @param registryKey The {@link net.minecraft.core.registries.Registries} registry resource key.
     * @param <T>         The type of the registry entries, e.g. {@link net.minecraft.sounds.SoundEvent}.
     * @return a scoped {@link BalmRegistrar} that can be used to register entries to this registry.
     * @see BalmRegistrars#blocks(Consumer)
     * @see BalmRegistrars#blockEntityTypes(Consumer)
     * @see BalmRegistrars#items(Consumer)
     * @see BalmRegistrars#creativeModeTabs(Consumer)
     * @see BalmRegistrars#recipeTypes(Consumer)
     */
    public <T> BalmRegistrar.Scoped<T> registrar(ResourceKey<? extends Registry<T>> registryKey) {
        return runtime.registrar(registryKey, namespace);
    }

    /**
     * Use this to register registry objects that are not covered by the convenient factories.
     * Creates a scoped registrar for a specific registry and namespace.
     *
     * @param registryKey the {@link net.minecraft.core.registries.Registries} registry resource key.
     * @param initializer callback that receives a scoped registrar for registering entries to this registry.
     * @param <T>         the type of the registry entries, e.g. {@link net.minecraft.sounds.SoundEvent}.
     * @see BalmRegistrars#blocks(Consumer)
     * @see BalmRegistrars#blockEntityTypes(Consumer)
     * @see BalmRegistrars#items(Consumer)
     * @see BalmRegistrars#creativeModeTabs(Consumer)
     * @see BalmRegistrars#recipeTypes(Consumer)
     */
    public <T> void registrar(ResourceKey<? extends Registry<T>> registryKey, Consumer<BalmRegistrar.Scoped<T>> initializer) {
        initializer.accept(runtime.registrar(registryKey, namespace));
    }

    public void registerModule(BalmModule module) {
        runtime.registerModule(this, module);
    }
}
