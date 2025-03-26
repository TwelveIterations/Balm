package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.common.CommonCapabilities;
import net.blay09.mods.balm.common.command.BalmCommand;
import net.blay09.mods.balm.common.config.ConfigSync;
import net.blay09.mods.balm.common.config.ExampleDeclarativeConfig;
import net.blay09.mods.balm.common.config.ExampleReflectionConfig;
import net.blay09.mods.balm.common.resources.ConfigResourceCondition;
import net.blay09.mods.balm.neoforge.compat.hudinfo.TheOneProbeModCompat;
import net.blay09.mods.balm.neoforge.network.NeoForgeBalmNetworking;
import net.blay09.mods.balm.neoforge.provider.NeoForgeBalmCapabilities;
import net.blay09.mods.balm.neoforge.world.NeoForgeBalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.Capabilities;

@Mod("balm")
public class NeoForgeBalm {

    public NeoForgeBalm(IEventBus modBus) {
        ((NeoForgeBalmRuntime) Balm.getRuntime()).initializeRuntime();

        ConfigSync.initialize();
        Balm.getConfig().registerConfig(ExampleDeclarativeConfig.schema);
        Balm.getConfig().registerConfig(ExampleReflectionConfig.class);
        Balm.getCommands().register(BalmCommand::register);

        Balm.getResources().registerResourceCondition(ResourceLocation.fromNamespaceAndPath("balm", "config"), ConfigResourceCondition.CODEC);
        DeferredRegisters.register("balm", modBus);

        NeoForgeBalmWorldGen.initializeBalmBiomeModifiers(modBus);
        modBus.addListener(this::enqueueIMC);

        CommonCapabilities.initialize(Balm.getCapabilities());

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
