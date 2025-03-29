package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.AbstractBalmConfig;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.common.command.BalmCommand;
import net.blay09.mods.balm.config.ExampleConfig;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.capability.ForgeCommonCapabilities;
import net.blay09.mods.balm.forge.client.ForgeBalmClient;
import net.blay09.mods.balm.forge.provider.ForgeBalmProviders;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;

@Mod("balm")
public class ForgeBalm {

    public ForgeBalm(FMLJavaModLoadingContext context) {
        Balm.registerModule(new ForgeCommonCapabilities());
        ((ForgeBalmRuntime) Balm.getRuntime()).initializeRuntime();

        ((AbstractBalmConfig) Balm.getConfig()).initialize();
        ExampleConfig.initialize();

        final var modEventBus = context.getModEventBus();
        ForgeBalmWorldGen.initializeBalmBiomeModifiers(modEventBus);
        modEventBus.addListener(ForgeBalmClient::onInitializeClient);

        ForgeBalmProviders providers = (ForgeBalmProviders) Balm.getProviders();
        providers.register(IItemHandler.class, new CapabilityToken<>() {
        });
        providers.register(IFluidHandler.class, new CapabilityToken<>() {
        });
        providers.register(IFluidHandlerItem.class, new CapabilityToken<>() {
        });
        providers.register(IEnergyStorage.class, new CapabilityToken<>() {
        });
        providers.register(Container.class, new CapabilityToken<>() {
        });
        providers.register(FluidTank.class, new CapabilityToken<>() {
        });
        providers.register(EnergyStorage.class, new CapabilityToken<>() {
        });

        final var capabilities = (ForgeBalmCapabilities) Balm.getCapabilities();
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("forge", "item_handler"), IItemHandler.class, ForgeCapabilities.ITEM_HANDLER);
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("forge", "fluid_handler"), IFluidHandler.class, ForgeCapabilities.FLUID_HANDLER);
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("forge", "energy_storage"), IEnergyStorage.class, ForgeCapabilities.ENERGY);
    }
}
