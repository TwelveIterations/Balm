package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Consumer;

public interface BalmClientRuntime<TLoadContext extends BalmRuntimeLoadContext> {
    @Deprecated
    BalmRenderers getRenderers();

    @Deprecated
    default BalmScreens getScreens() {
        return BalmScreens.LEGACY;
    }

    @Deprecated
    BalmModels getModels();

    @Deprecated
    default BalmKeyMappings getKeyMappings() {
        return BalmKeyMappings.LEGACY;
    }

    @Deprecated
    default void initializeMod(String modId, TLoadContext context, Runnable initializer) {
        initializeMod(modId, context, (registrars) -> initializer.run());
    }

    void initializeMod(String modId, TLoadContext context, Consumer<BalmClientRegistrars> initializer);

    default void initializeModule(BalmClientModule module) {
        final var modId = module.getId().getNamespace();
        module.registerEvents(Balm.events());
        resourceReloadListeners(modId, module::registerClientReloadListeners);
        module.registerRenderers(getRenderers().scoped(modId));
        blockColors(modId, module::registerBlockColors);
        blockRenderTypes(modId, module::registerBlockRenderTypes);
        blockEntityRenderers(modId, module::registerBlockEntityRenderers);
        entityRenderers(modId, module::registerEntityRenderers);
        particleProviders(modId, module::registerParticleProviders);
        modelLayers(modId, module::registerModelLayers);

        module.registerScreens(getScreens().scoped(modId));
        menuScreens(modId, module::registerMenuScreens);

        module.registerModels(getModels().scoped(modId));
        blockStateModels(modId, module::registerBlockStateModels);

        module.registerKeyMappings(getKeyMappings().scoped(modId));
        keyMappings(modId, module::registerKeyMappings);

        module.initialize();
    }

    boolean isReady();

    void onRuntimeAvailable(Runnable callback);

    @Deprecated
    default void registerModule(BalmClientModule module) {
        registerModule(new BalmClientRegistrars(this, module.getId().getNamespace()), module);
    }

    void registerModule(BalmClientRegistrars registrars, BalmClientModule module);

    @Deprecated
    void addResourceReloadListener(Identifier identifier, PreparableReloadListener reloadListener);

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
