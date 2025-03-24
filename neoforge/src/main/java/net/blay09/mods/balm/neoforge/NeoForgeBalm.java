package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.common.CommonCapabilities;
import net.blay09.mods.balm.common.command.BalmCommand;
import net.blay09.mods.balm.common.config.ConfigSync;
import net.blay09.mods.balm.common.config.ExampleDeclarativeConfig;
import net.blay09.mods.balm.common.config.ExampleReflectionConfig;
import net.blay09.mods.balm.neoforge.compat.hudinfo.TheOneProbeModCompat;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmCapabilities;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;

@Mod("balm")
public class NeoForgeBalm {

    /* TODO shouldn't be necessary anymore after we register them in CapabilityTypes
    public static final BlockCapability<Container, Direction> CONTAINER_CAPABILITY = BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath("balm",
            "container"), Container.class);
    public static final BlockCapability<FluidTank, Direction> FLUID_TANK_CAPABILITY = BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath(
            "balm",
            "fluid_tank"), FluidTank.class);
    public static final BlockCapability<EnergyStorage, Direction> ENERGY_STORAGE_CAPABILITY = BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath(
            "balm",
            "energy_storage"), EnergyStorage.class);*/

    public NeoForgeBalm(IEventBus modBus) {
        ((NeoForgeBalmRuntime) Balm.getRuntime()).initializeRuntime();

        ConfigSync.initialize();
        Balm.getConfig().registerConfig(ExampleDeclarativeConfig.schema);
        Balm.getConfig().registerConfig(ExampleReflectionConfig.class);
        Balm.getCommands().register(BalmCommand::register);

        NeoForgeBalmWorldGen.initializeBalmBiomeModifiers(modBus);
        modBus.addListener(this::enqueueIMC);

        CommonCapabilities.initialize(Balm.getCapabilities());

        NeoForgeBalmCapabilities capabilities = (NeoForgeBalmCapabilities) Balm.getCapabilities();
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("neoforge", "item_handler"), Capabilities.ItemHandler.BLOCK);
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("neoforge", "fluid_handler"), Capabilities.FluidHandler.BLOCK);
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("neoforge", "fluid_handler_item"), Capabilities.FluidHandler.ITEM);
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("neoforge", "energy_storage"), Capabilities.EnergyStorage.BLOCK);
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        if (Balm.isModLoaded("theoneprobe")) {
            TheOneProbeModCompat.register();
        }
    }

}
