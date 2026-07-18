package net.blay09.mods.balm.neoforge.client.platform.runtime.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.BalmClientTooltipComponentRegistrar;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.BalmRangeSelectItemModelPropertyRegistrar;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.commands.BalmClientCommands;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenRegistrar;
import net.blay09.mods.balm.client.platform.config.internal.BalmConfigScreenRegistrarImpl;
import net.blay09.mods.balm.client.platform.runtime.internal.CommonBalmClientRuntime;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.neoforge.client.color.block.internal.NeoForgeBalmBlockColorRegistrar;
import net.blay09.mods.balm.neoforge.client.gui.screens.inventory.internal.NeoForgeBalmMenuScreenRegistrar;
import net.blay09.mods.balm.neoforge.client.internal.NeoForgeBalmClientTooltipComponentRegistrar;
import net.blay09.mods.balm.neoforge.client.internal.NeoForgeBalmKeyMappingRegistrar;
import net.blay09.mods.balm.neoforge.client.internal.NeoForgeBalmRangeSelectItemModelPropertyRegistrar;
import net.blay09.mods.balm.neoforge.client.internal.commands.NeoForgeBalmClientCommands;
import net.blay09.mods.balm.neoforge.client.model.geom.internal.NeoForgeBalmModelLayerRegistrar;
import net.blay09.mods.balm.neoforge.client.particle.internal.NeoForgeBalmParticleProviderRegistrar;
import net.blay09.mods.balm.neoforge.client.platform.config.internal.BalmNeoForgeConfigurationScreen;
import net.blay09.mods.balm.neoforge.client.platform.config.internal.NeoForgeBalmConfigScreenProviders;
import net.blay09.mods.balm.neoforge.client.platform.event.internal.NeoForgeBalmClientEventMappings;
import net.blay09.mods.balm.neoforge.client.renderer.block.model.internal.NeoForgeBalmBlockStateModelRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.blockentity.internal.NeoForgeBalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.neoforge.client.renderer.entity.internal.NeoForgeBalmEntityRendererRegistrar;
import net.blay09.mods.balm.neoforge.platform.event.internal.ModBusEventRegisters;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.blay09.mods.balm.neoforge.server.packs.resources.internal.NeoForgeBalmClientResourceReloadListenerRegistrar;
import net.blay09.mods.balm.platform.config.BalmConfig;
import net.blay09.mods.balm.platform.config.internal.BalmConfigScreenProviders;
import net.blay09.mods.balm.platform.runtime.internal.BalmLoadContexts;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

import java.util.function.Consumer;

public class NeoForgeBalmClientRuntime extends CommonBalmClientRuntime<NeoForgeLoadContext> {

    private final BalmClientCommands clientCommands = new NeoForgeBalmClientCommands();

    public NeoForgeBalmClientRuntime() {
        NeoForgeBalmClientEventMappings.bind();

        BalmConfigScreenProviders.register(BalmConfig.DEFAULT_CONFIG_SCREEN_PROVIDER_ID, modId -> {
            if (Balm.config().getSchemasByNamespace(modId).isEmpty()) {
                return null;
            }

            return parent -> ModList.get().getModContainerById(modId)
                    .map(modContainer -> new ConfigurationScreen(modContainer, parent, BalmNeoForgeConfigurationScreen::new))
                    .orElse(null);
        });
    }

    @Override
    public void initializeMod(String modId, NeoForgeLoadContext context, Consumer<BalmClientRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmClientRegistrars(this, modId));

        final var modEventBus = context.modBus();
        ModBusEventRegisters.register(modId, modEventBus);
    }

    @Override
    public BalmClientCommands clientCommands() {
        return clientCommands;
    }

    @Override
    public void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> initializer.accept(new NeoForgeBalmBlockEntityRendererRegistrar(event)));
            }
        });
    }

    @Override
    public void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> initializer.accept(new NeoForgeBalmEntityRendererRegistrar(event)));
            }
        });
    }

    @Override
    public void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((RegisterMenuScreensEvent event) -> initializer.accept(new NeoForgeBalmMenuScreenRegistrar(event)));
            }
        });
    }

    @Override
    public void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((ModelEvent.RegisterStandalone event) -> initializer.accept(new NeoForgeBalmBlockStateModelRegistrar(event)));
            }
        });
    }

    @Override
    public void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((RegisterKeyMappingsEvent event) -> initializer.accept(new NeoForgeBalmKeyMappingRegistrar(event)));
            }
        });
    }

    @Override
    public void modelLayers(String namespace, Consumer<BalmModelLayerRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) -> initializer.accept(new NeoForgeBalmModelLayerRegistrar(event)));
            }
        });
    }

    @Override
    public void blockColors(String namespace, Consumer<BalmBlockColorRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((RegisterColorHandlersEvent.BlockTintSources event) -> initializer.accept(new NeoForgeBalmBlockColorRegistrar(event)));
            }
        });
    }

    @Override
    public void particleProviders(String namespace, Consumer<BalmParticleProviderRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((RegisterParticleProvidersEvent event) -> initializer.accept(new NeoForgeBalmParticleProviderRegistrar(event)));
            }
        });
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmClientResourceReloadListenerRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((AddClientReloadListenersEvent event) -> initializer.accept(new NeoForgeBalmClientResourceReloadListenerRegistrar(namespace, event)));
            }
        });
    }

    @Override
    public void clientTooltipComponents(String namespace, Consumer<BalmClientTooltipComponentRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((RegisterClientTooltipComponentFactoriesEvent event) -> initializer.accept(new NeoForgeBalmClientTooltipComponentRegistrar(event)));
            }
        });
    }

    @Override
    public void rangeSelectItemModelProperties(String namespace, Consumer<BalmRangeSelectItemModelPropertyRegistrar> initializer) {
        BalmLoadContexts.get(namespace).ifPresent(context -> {
            if (context instanceof NeoForgeLoadContext(ModContainer modContainer, IEventBus modBus)) {
                modBus.addListener((RegisterRangeSelectItemModelPropertyEvent event) -> initializer.accept(new NeoForgeBalmRangeSelectItemModelPropertyRegistrar(event)));
            }
        });
    }

    @Override
    public void configScreen(String namespace, Consumer<BalmConfigScreenRegistrar> initializer) {
        super.configScreen(namespace, initializer);

        if (BalmConfigScreenProviders.hasModOverride(namespace)) {
            ModList.get().getModContainerById(namespace).ifPresent(NeoForgeBalmConfigScreenProviders::initializeConfigurationScreen);
        }
    }
}
