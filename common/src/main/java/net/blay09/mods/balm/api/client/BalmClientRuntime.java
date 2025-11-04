package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Consumer;

public interface BalmClientRuntime<TLoadContext extends BalmRuntimeLoadContext> {
    BalmRenderers getRenderers();

    @Deprecated
    default BalmScreens getScreens() {
        return BalmScreens.LEGACY;
    }

    BalmModels getModels();

    BalmKeyMappings getKeyMappings();

    void initializeMod(String modId, TLoadContext context, Runnable initializer);

    default void initializeModule(BalmClientModule module) {
        final var modId = module.getId().getNamespace();
        module.registerEvents(Balm.events());
        module.registerRenderers(getRenderers().scoped(modId));
        blockEntityRenderers(modId, module::registerBlockEntityRenderers);
        entityRenderers(modId, module::registerEntityRenderers);

        module.registerScreens(getScreens().scoped(modId));
        menuScreens(modId, module::registerMenuScreens);

        module.registerModels(getModels().scoped(modId));
        blockStateModels(modId, module::registerModels);

        module.registerKeyMappings(getKeyMappings().scoped(modId));
        module.initialize();
    }

    boolean isReady();

    void onRuntimeAvailable(Runnable callback);

    void registerModule(BalmClientModule module);

    void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener);

    void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer);

    void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer);

    void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer);

    void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer);
}
