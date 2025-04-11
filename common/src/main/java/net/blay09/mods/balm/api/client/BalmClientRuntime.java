package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

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

    void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener);
}
