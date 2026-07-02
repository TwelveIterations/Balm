package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmRuntimeLoadContext;
import net.blay09.mods.balm.api.client.commands.BalmClientCommands;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.LegacyNamespaceResolver;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.client.CommonBalmClientRuntime;
import net.blay09.mods.balm.forge.ForgeLoadContext;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.blay09.mods.balm.forge.client.internal.commands.ForgeBalmClientCommands;
import net.blay09.mods.balm.forge.client.keymappings.ForgeBalmKeyMappings;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmModels;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmRenderers;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmTextures;
import net.blay09.mods.balm.forge.client.screen.ForgeBalmScreens;
import net.blay09.mods.balm.forge.event.ForgeBalmClientEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeBalmClientRuntime extends CommonBalmClientRuntime<ForgeLoadContext> {

    private final NamespaceResolver legacyNamespaceResolver = new LegacyNamespaceResolver(() -> ModLoadingContext.get().getActiveNamespace());
    private final BalmRenderers renderers = new ForgeBalmRenderers(legacyNamespaceResolver);
    private final BalmScreens screens = new ForgeBalmScreens(legacyNamespaceResolver);
    private final BalmKeyMappings keyMappings = new ForgeBalmKeyMappings(legacyNamespaceResolver);
    private final BalmModels models = new ForgeBalmModels(legacyNamespaceResolver);
    private final BalmClientCommands clientCommands = new ForgeBalmClientCommands();

    @Deprecated(since = "1.21.5")
    private final BalmTextures textures = new ForgeBalmTextures();

    public ForgeBalmClientRuntime() {
        ForgeBalmClientEvents.registerEvents(((ForgeBalmEvents) Balm.getEvents()));
    }

    @Override
    public BalmRenderers getRenderers() {
        return renderers;
    }

    @Override
    @Deprecated(since = "1.21.5")
    public BalmTextures getTextures() {
        return textures;
    }

    @Override
    public BalmScreens getScreens() {
        return screens;
    }

    @Override
    public BalmModels getModels() {
        return models;
    }

    @Override
    public BalmClientCommands clientCommands() {
        return clientCommands;
    }

    @Override
    public BalmKeyMappings getKeyMappings() {
        return keyMappings;
    }

    @Override
    public void initializeMod(String modId, BalmRuntimeLoadContext context, Runnable initializer) {
        ForgeLoadContext forgeLoadContext;
        if (context instanceof ForgeLoadContext) {
            forgeLoadContext = (ForgeLoadContext) context;
        } else {
            forgeLoadContext = new ForgeLoadContext(FMLJavaModLoadingContext.get().getModEventBus());
        }
        BalmLoadContexts.register(modId, forgeLoadContext);

        initializer.run();

        final var modEventBus = forgeLoadContext.modEventBus();
        ModBusEventRegisters.register(modId, modEventBus);
    }

    @Override
    public void addResourceReloadListener(ResourceLocation identifier, PreparableReloadListener reloadListener) {
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager reloadableResourceManager) {
            reloadableResourceManager.registerReloadListener(reloadListener);
        }
    }
}
