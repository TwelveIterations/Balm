package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface BalmClientRuntime {
    BalmRenderers getRenderers();

    BalmScreens getScreens();

    BalmModels getModels();

    BalmKeyMappings getKeyMappings();

    void initializeMod(String modId, Runnable initializer);

    default void initializeModule(BalmClientModule module) {
        module.registerEvents(Balm.getEvents());
        module.registerRenderers(getRenderers());
        module.registerScreens(getScreens());
        module.registerModels(getModels());
        module.registerKeyMappings(getKeyMappings());
        module.initialize();
    }

    boolean isReady();

    void onRuntimeAvailable(Runnable callback);

    void registerModule(BalmClientModule module);

    void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener);

    @Deprecated
    BalmTextures getTextures();
}
