package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.config.ExampleDeclarativeConfig;
import net.blay09.mods.balm.common.config.ExampleReflectionConfig;
import net.blay09.mods.balm.neoforge.capability.NeoForgeBalmCapabilities;
import net.blay09.mods.balm.neoforge.compat.hudinfo.TheOneProbeModCompat;
import net.blay09.mods.balm.neoforge.network.NeoForgeBalmNetworking;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmProviders;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.IItemHandler;

@Mod("balm")
public class NeoForgeBalm {

    public static final BlockCapability<Container, Direction> CONTAINER_CAPABILITY = BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath("balm",
            "container"), Container.class);
    // Backwards compatibility requires us to use Void as context. Fixed in 1.21.5+
    public static final BlockCapability<FluidTank, Void> FLUID_TANK_CAPABILITY = BlockCapability.create(ResourceLocation.fromNamespaceAndPath(
            "balm",
            "fluid_tank"), FluidTank.class, Void.class);
    public static final BlockCapability<EnergyStorage, Direction> ENERGY_STORAGE_CAPABILITY = BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath(
            "balm",
            "energy_storage"), EnergyStorage.class);

    public NeoForgeBalm(IEventBus modBus) {
        BalmLoadContexts.register("balm", new NeoForgeLoadContext(modBus));

        ((NeoForgeBalmRuntime) Balm.getRuntime()).initializeRuntime();

        Balm.getConfig().registerConfig(ExampleDeclarativeConfig.schema);
        Balm.getConfig().registerConfig(ExampleReflectionConfig.class);

        DeferredRegisters.register("balm", modBus);

        NeoForgeBalmWorldGen.initializeBalmBiomeModifiers(modBus);
        modBus.addListener(this::enqueueIMC);

        final var providers = (NeoForgeBalmProviders) Balm.getProviders();
        providers.registerBlockProvider(IItemHandler.class, Capabilities.ItemHandler.BLOCK);
        providers.registerBlockProvider(IFluidHandler.class, Capabilities.FluidHandler.BLOCK);
        providers.registerItemProvider(IFluidHandlerItem.class, Capabilities.FluidHandler.ITEM);
        providers.registerBlockProvider(IEnergyStorage.class, Capabilities.EnergyStorage.BLOCK);

        providers.registerBlockProvider(Container.class, CONTAINER_CAPABILITY);
        providers.registerBlockProvider(FluidTank.class, FLUID_TANK_CAPABILITY);
        providers.registerBlockProvider(EnergyStorage.class, ENERGY_STORAGE_CAPABILITY);

        NeoForgeBalmCapabilities capabilities = (NeoForgeBalmCapabilities) Balm.getCapabilities();
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("neoforge", "item_handler"), Capabilities.ItemHandler.BLOCK);
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("neoforge", "fluid_handler"), Capabilities.FluidHandler.BLOCK);
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("neoforge", "energy_storage"), Capabilities.EnergyStorage.BLOCK);

        ((NeoForgeBalmNetworking) Balm.getNetworking()).register("balm", modBus);
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        if (Balm.isModLoaded("theoneprobe")) {
            TheOneProbeModCompat.register();
        }
    }

}
