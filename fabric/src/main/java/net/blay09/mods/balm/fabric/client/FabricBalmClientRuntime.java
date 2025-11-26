package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.client.CommonBalmClientRuntime;
import net.blay09.mods.balm.fabric.client.internal.FabricBalmKeyMappingRegistrar;
import net.blay09.mods.balm.fabric.client.internal.color.block.FabricBalmBlockColorRegistrar;
import net.blay09.mods.balm.fabric.client.internal.gui.screens.inventory.FabricBalmMenuScreenRegistrar;
import net.blay09.mods.balm.fabric.client.internal.model.geom.FabricBalmModelLayerRegistrar;
import net.blay09.mods.balm.fabric.client.internal.particle.FabricBalmParticleProviderRegistrar;
import net.blay09.mods.balm.fabric.client.internal.renderer.block.model.FabricBalmBlockStateModelRegistrar;
import net.blay09.mods.balm.fabric.client.internal.renderer.blockentity.FabricBalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.fabric.client.internal.renderer.chunk.FabricBalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.fabric.client.internal.renderer.entity.FabricBalmEntityRendererRegistrar;
import net.blay09.mods.balm.fabric.client.keymappings.FabricBalmKeyMappings;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmModels;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmRenderers;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmTextures;
import net.blay09.mods.balm.fabric.client.screen.FabricBalmScreens;
import net.blay09.mods.balm.fabric.event.FabricBalmEvents;
import net.blay09.mods.balm.fabric.event.client.FabricBalmClientEvents;
import net.blay09.mods.balm.fabric.server.packs.resources.internal.FabricBalmClientResourceReloadListenerRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class FabricBalmClientRuntime extends CommonBalmClientRuntime<EmptyLoadContext> {

    private static final Logger logger = LoggerFactory.getLogger(FabricBalmClientRuntime.class);
    private static final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> {
        throw new UnsupportedOperationException("No default namespace available");
    });
    private final BalmRenderers renderers = new FabricBalmRenderers(legacyNamespaceResolver);
    @Deprecated
    private final BalmTextures textures = new FabricBalmTextures();
    @Deprecated
    private final BalmScreens screens = new FabricBalmScreens(legacyNamespaceResolver);
    @Deprecated
    private final BalmKeyMappings keyMappings = createKeyMappingsBindings();
    private final BalmModels models = new FabricBalmModels(legacyNamespaceResolver);

    public FabricBalmClientRuntime() {
        FabricBalmClientEvents.registerEvents(((FabricBalmEvents) Balm.getEvents()));
    }

    @Deprecated
    private static BalmKeyMappings createKeyMappingsBindings() {
        if (Balm.isModLoaded("amecs")) {
            try {
                Class.forName("de.siphalor.amecs.api.AmecsKeyBinding");
                try {
                    Class<?> amecs = Class.forName("net.blay09.mods.balm.fabric.compat.AmecsBalmKeyMappings");
                    return (BalmKeyMappings) amecs.getConstructor(NamespaceResolver.class).newInstance(legacyNamespaceResolver);
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    logger.error("Failed to initialize amecs key mappings for Balm", e);
                }
            } catch (ClassNotFoundException ignored) {
                // we silently ignore amecs if the api is missing which would only really be the case in a devenv pulling from cursemaven
            }
        }
        return new FabricBalmKeyMappings(legacyNamespaceResolver);
    }

    @Override
    public BalmRenderers getRenderers() {
        return renderers;
    }

    @Override
    @Deprecated
    public BalmTextures getTextures() {
        return textures;
    }

    @Override
    @Deprecated
    public BalmScreens getScreens() {
        return screens;
    }

    @Override
    @Deprecated
    public BalmKeyMappings getKeyMappings() {
        return keyMappings;
    }

    @Override
    public BalmModels getModels() {
        return models;
    }

    @Override
    public void initializeMod(String modId, EmptyLoadContext context, Consumer<BalmClientRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmClientRegistrars(this, modId));
    }

    @Override
    public void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
                return reloadListener.reload(barrier, manager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            }
        });
    }


    @Override
    public void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer) {
        initializer.accept(FabricBalmBlockEntityRendererRegistrar.INSTANCE);
    }

    @Override
    public void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer) {
        initializer.accept(FabricBalmEntityRendererRegistrar.INSTANCE);
    }

    @Override
    public void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer) {
        ModelLoadingPlugin.register(context -> initializer.accept(new FabricBalmBlockStateModelRegistrar(context)));
    }

    @Override
    public void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer) {
        initializer.accept(FabricBalmMenuScreenRegistrar.INSTANCE);
    }

    @Override
    public void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer) {
        initializer.accept(FabricBalmKeyMappingRegistrar.INSTANCE);
    }

    @Override
    public void modelLayers(String namespace, Consumer<BalmModelLayerRegistrar> initializer) {
        initializer.accept(FabricBalmModelLayerRegistrar.INSTANCE);
    }

    @Override
    public void blockColors(String namespace, Consumer<BalmBlockColorRegistrar> initializer) {
        initializer.accept(FabricBalmBlockColorRegistrar.INSTANCE);
    }

    @Override
    public void particleProviders(String namespace, Consumer<BalmParticleProviderRegistrar> initializer) {
        initializer.accept(FabricBalmParticleProviderRegistrar.INSTANCE);
    }

    @Override
    public void blockRenderTypes(String namespace, Consumer<BalmBlockRenderTypeRegistrar> initializer) {
        initializer.accept(FabricBalmBlockRenderTypeRegistrar.INSTANCE);
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmClientResourceReloadListenerRegistrar> initializer) {
        initializer.accept(new FabricBalmClientResourceReloadListenerRegistrar(namespace));
    }

}
