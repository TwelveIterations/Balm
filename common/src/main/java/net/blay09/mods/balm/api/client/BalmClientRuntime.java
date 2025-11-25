package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
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
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Consumer;

public interface BalmClientRuntime<TLoadContext extends BalmRuntimeLoadContext> {
    BalmRenderers getRenderers();

    BalmScreens getScreens();

    BalmModels getModels();

    BalmKeyMappings getKeyMappings();

    void initializeMod(String modId, TLoadContext context, Runnable initializer);

    default void initializeModule(BalmClientModule module) {
        final var modId = module.getId().getNamespace();
        module.registerEvents(Balm.getEvents());
        module.registerRenderers(getRenderers().scoped(modId));
        module.registerScreens(getScreens().scoped(modId));
        module.registerModels(getModels().scoped(modId));
        module.registerKeyMappings(getKeyMappings().scoped(modId));
        module.initialize();
    }

    boolean isReady();

    void onRuntimeAvailable(Runnable callback);

    void registerModule(BalmClientModule module);

    void registerModule(BalmClientRegistrars registrars, BalmClientModule module);

    void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener);

    /**
     * @deprecated No more purpose. Register textures to atlases via resource pack instead.
     */
    @Deprecated(since = "1.21.5")
    BalmTextures getTextures();

    void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer);

    void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer);

    void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer);

    void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer);

    void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer);

    void modelLayers(String namespace, Consumer<BalmModelLayerRegistrar> initializer);

    void blockColors(String namespace, Consumer<BalmBlockColorRegistrar> initializer);

    void particleProviders(String namespace, Consumer<BalmParticleProviderRegistrar> initializer);

    void blockRenderTypes(String namespace, Consumer<BalmBlockRenderTypeRegistrar> initializer);

    void resourceReloadListeners(String namespace, Consumer<BalmClientResourceReloadListenerRegistrar> initializer);
}
