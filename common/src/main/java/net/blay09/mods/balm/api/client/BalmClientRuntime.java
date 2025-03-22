package net.blay09.mods.balm.api.client;

import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.module.BalmClientModule;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;

public interface BalmClientRuntime<TLoadContext extends BalmRuntimeLoadContext> {
    BalmRenderers getRenderers();

    BalmScreens getScreens();

    BalmModels getModels();

    /**
     * @deprecated Use Kuma instead.
     */
    @Deprecated
    BalmKeyMappings getKeyMappings();

    void initialize(String modId, TLoadContext context, Runnable initializer);

    default void initializeModule(BalmClientModule module) {
        module.registerEvents(Balm.getEvents());
        module.registerRenderers(getRenderers());
        module.registerScreens(getScreens());
        module.registerModels(getModels());
        module.registerKeyMappings(getKeyMappings());
        module.initialize();
    }
}
