package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.EmptyLoadContext;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.config.ExampleDeclarativeConfig;
import net.blay09.mods.balm.common.config.ExampleReflectionConfig;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.capability.ForgeCommonCapabilities;
import net.blay09.mods.balm.forge.client.ForgeBalmClient;
import net.blay09.mods.balm.forge.compat.hudinfo.TheOneProbeModCompat;
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
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;

@Mod("balm")
public class ForgeBalm {

    public ForgeBalm() {
        final var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BalmLoadContexts.register("balm", new ForgeLoadContext(modEventBus));

        Balm.registerModule(new ForgeCommonCapabilities());
        ((ForgeBalmRuntime) Balm.getRuntime()).initializeRuntime();

        Balm.getConfig().registerConfig(ExampleDeclarativeConfig.schema);
        Balm.getConfig().registerConfig(ExampleReflectionConfig.class);

        DeferredRegisters.register("balm", modEventBus);
        ModBusEventRegisters.register("balm", modEventBus);

        ForgeBalmWorldGen.initializeBalmBiomeModifiers(modEventBus);
        modEventBus.addListener(ForgeBalmClient::onInitializeClient);
        modEventBus.addListener(this::enqueueIMC);

        final var providers = (ForgeBalmProviders) Balm.getProviders();
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
        capabilities.addExistingType(new ResourceLocation("forge", "item_handler"), IItemHandler.class, ForgeCapabilities.ITEM_HANDLER);
        capabilities.addExistingType(new ResourceLocation("forge", "fluid_handler"), IFluidHandler.class, ForgeCapabilities.FLUID_HANDLER);
        capabilities.addExistingType(new ResourceLocation("forge", "energy_storage"), IEnergyStorage.class, ForgeCapabilities.ENERGY);
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        if (Balm.isModLoaded("theoneprobe")) {
            TheOneProbeModCompat.register();
        }
    }
}
