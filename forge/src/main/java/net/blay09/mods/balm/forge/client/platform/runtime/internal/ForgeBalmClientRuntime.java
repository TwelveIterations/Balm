package net.blay09.mods.balm.forge.client.platform.runtime.internal;

import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.BalmKeyMappingRegistrar;
import net.blay09.mods.balm.client.BalmClientTooltipComponentRegistrar;
import net.blay09.mods.balm.client.BalmRangeSelectItemModelPropertyRegistrar;
import net.blay09.mods.balm.client.color.block.BalmBlockColorRegistrar;
import net.blay09.mods.balm.client.commands.BalmClientCommands;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.balm.client.model.geom.BalmModelLayerRegistrar;
import net.blay09.mods.balm.client.particle.BalmParticleProviderRegistrar;
import net.blay09.mods.balm.client.platform.config.BalmConfigScreenRegistrar;
import net.blay09.mods.balm.client.platform.runtime.internal.CommonBalmClientRuntime;
import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.forge.client.internal.ForgeBalmKeyMappingRegistrar;
import net.blay09.mods.balm.forge.client.internal.ForgeBalmClientTooltipComponentRegistrar;
import net.blay09.mods.balm.forge.client.internal.ForgeBalmRangeSelectItemModelPropertyRegistrar;
import net.blay09.mods.balm.forge.client.internal.commands.ForgeBalmClientCommands;
import net.blay09.mods.balm.forge.platform.runtime.ForgeLoadContext;
import net.blay09.mods.balm.forge.platform.event.internal.ModBusEventRegisters;
import net.blay09.mods.balm.forge.client.color.block.internal.ForgeBalmBlockColorRegistrar;
import net.blay09.mods.balm.forge.client.event.internal.ForgeBalmClientEventMappings;
import net.blay09.mods.balm.forge.client.gui.screens.inventory.internal.ForgeBalmMenuScreenRegistrar;
import net.blay09.mods.balm.forge.client.model.geom.internal.ForgeBalmModelLayerRegistrar;
import net.blay09.mods.balm.forge.client.particle.internal.ForgeBalmParticleProviderRegistrar;
import net.blay09.mods.balm.forge.client.renderer.block.model.internal.ForgeBalmBlockStateModelRegistrar;
import net.blay09.mods.balm.forge.client.renderer.blockentity.internal.ForgeBalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.forge.client.renderer.entity.internal.ForgeBalmEntityRendererRegistrar;
import net.blay09.mods.balm.forge.server.packs.resources.internal.ForgeBalmClientResourceReloadListenerRegistrar;
import net.blay09.mods.balm.platform.config.internal.BalmConfigScreenProviders;
import net.blay09.mods.balm.platform.runtime.internal.BalmLoadContexts;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Consumer;

public class ForgeBalmClientRuntime extends CommonBalmClientRuntime<ForgeLoadContext> {

    private final BalmClientCommands clientCommands = new ForgeBalmClientCommands();

    public ForgeBalmClientRuntime() {
        ForgeBalmClientEventMappings.bind();
    }

    @Override
    public void initializeMod(String modId, ForgeLoadContext context, Consumer<BalmClientRegistrars> initializer) {
        BalmLoadContexts.register(modId, context);

        initializer.accept(new BalmClientRegistrars(this, modId));

        ModBusEventRegisters.register(modId, context.modBusGroup());
    }

    @Override
    public BalmClientCommands clientCommands() {
        return clientCommands;
    }

    @Override
    public void blockEntityRenderers(String namespace, Consumer<BalmBlockEntityRendererRegistrar> initializer) {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener((event) -> initializer.accept(new ForgeBalmBlockEntityRendererRegistrar(event)));
    }

    @Override
    public void entityRenderers(String namespace, Consumer<BalmEntityRendererRegistrar> initializer) {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener((event) -> initializer.accept(new ForgeBalmEntityRendererRegistrar(event)));
    }

    @Override
    public void blockStateModels(String namespace, Consumer<BalmBlockStateModelRegistrar> initializer) {
        ModelEvent.RegisterModelStateDefinitions.BUS.addListener(event -> initializer.accept(new ForgeBalmBlockStateModelRegistrar(event)));
    }

    @Override
    public void menuScreens(String namespace, Consumer<BalmMenuScreenRegistrar> initializer) {
        final var busGroup = ModBusEventRegisters.getBusGroup(namespace);
        FMLClientSetupEvent.getBus(busGroup)
                .addListener((event)
                        -> event.enqueueWork(() ->
                        initializer.accept(ForgeBalmMenuScreenRegistrar.INSTANCE)));
    }

    @Override
    public void keyMappings(String namespace, Consumer<BalmKeyMappingRegistrar> initializer) {
        RegisterKeyMappingsEvent.BUS.addListener(event -> initializer.accept(new ForgeBalmKeyMappingRegistrar(event)));
    }

    @Override
    public void modelLayers(String namespace, Consumer<BalmModelLayerRegistrar> initializer) {
        EntityRenderersEvent.RegisterLayerDefinitions.BUS.addListener(event -> initializer.accept(new ForgeBalmModelLayerRegistrar(event)));
    }

    @Override
    public void blockColors(String namespace, Consumer<BalmBlockColorRegistrar> initializer) {
        RegisterColorHandlersEvent.Block.BUS.addListener(event -> initializer.accept(new ForgeBalmBlockColorRegistrar(event)));
    }

    @Override
    public void particleProviders(String namespace, Consumer<BalmParticleProviderRegistrar> initializer) {
        RegisterParticleProvidersEvent.BUS.addListener(event -> initializer.accept(new ForgeBalmParticleProviderRegistrar(event)));
    }

    @Override
    public void resourceReloadListeners(String namespace, Consumer<BalmClientResourceReloadListenerRegistrar> initializer) {
        initializer.accept(ForgeBalmClientResourceReloadListenerRegistrar.INSTANCE);
    }

    @Override
    public void clientTooltipComponents(String namespace, Consumer<BalmClientTooltipComponentRegistrar> initializer) {
        RegisterClientTooltipComponentFactoriesEvent.BUS.addListener(event -> initializer.accept(new ForgeBalmClientTooltipComponentRegistrar(event)));
    }

    @Override
    public void rangeSelectItemModelProperties(String namespace, Consumer<BalmRangeSelectItemModelPropertyRegistrar> initializer) {
        initializer.accept(ForgeBalmRangeSelectItemModelPropertyRegistrar.INSTANCE);
    }

    @Override
    public void configScreens(String namespace, Consumer<BalmConfigScreenRegistrar> initializer) {
        super.configScreen(namespace, initializer);

        if (BalmConfigScreenProviders.hasModOverride(namespace)) {
            ModList.get().getModContainerById(namespace).ifPresent(modContainer -> {
                if (modContainer.getCustomExtension(ConfigScreenHandler.ConfigScreenFactory.class).isEmpty()) {
                    modContainer.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory(parent -> {
                        final var factory = BalmConfigScreenProviders.getFactory(namespace);
                        return factory != null ? factory.create(parent) : null;
                    }));
                }
            });
        }
    }
}
