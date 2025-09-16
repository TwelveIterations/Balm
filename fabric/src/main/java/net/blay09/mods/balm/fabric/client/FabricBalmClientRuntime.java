package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.client.CommonBalmClientRuntime;
import net.blay09.mods.balm.fabric.client.keymappings.FabricBalmKeyMappings;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmModels;
import net.blay09.mods.balm.fabric.client.rendering.FabricBalmRenderers;
import net.blay09.mods.balm.fabric.client.screen.FabricBalmScreens;
import net.blay09.mods.balm.fabric.event.FabricBalmEvents;
import net.blay09.mods.balm.fabric.event.client.FabricBalmClientEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricBalmClientRuntime extends CommonBalmClientRuntime<EmptyLoadContext> {

    private static final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> {
        throw new UnsupportedOperationException("No default namespace available");
    });
    private final BalmRenderers renderers = new FabricBalmRenderers(legacyNamespaceResolver);
    private final BalmScreens screens = new FabricBalmScreens(legacyNamespaceResolver);
    private final BalmKeyMappings keyMappings = new FabricBalmKeyMappings();
    private final BalmModels models = new FabricBalmModels();

    public FabricBalmClientRuntime() {
        FabricBalmClientEvents.registerEvents(((FabricBalmEvents) Balm.getEvents()));
    }

    @Override
    public BalmRenderers getRenderers() {
        return renderers;
    }

    @Override
    public BalmScreens getScreens() {
        return screens;
    }

    @Override
    public BalmKeyMappings getKeyMappings() {
        return keyMappings;
    }

    @Override
    public BalmModels getModels() {
        return models;
    }

    @Override
    public void initializeMod(String modId, EmptyLoadContext context, Runnable initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.run();
    }

    @Override
    public void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return identifier;
            }

            @Override
            public CompletableFuture<Void> reload(SharedState sharedState, Executor executor, PreparationBarrier preparationBarrier, Executor executor2) {
                return reloadListener.reload(sharedState, executor, preparationBarrier, executor2);
            }
        });
    }

}
