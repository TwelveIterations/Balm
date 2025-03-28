package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.common.BalmLoadContexts;
import net.blay09.mods.balm.common.config.ExampleDeclarativeConfig;
import net.blay09.mods.balm.common.config.ExampleReflectionConfig;
import net.blay09.mods.balm.forge.capability.ForgeBalmCapabilities;
import net.blay09.mods.balm.forge.capability.ForgeCommonCapabilities;
import net.blay09.mods.balm.forge.client.ForgeBalmClient;
import net.blay09.mods.balm.forge.world.ForgeBalmWorldGen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;

@Mod("balm")
public class ForgeBalm {

    public ForgeBalm(FMLJavaModLoadingContext context) {
        BalmLoadContexts.register("balm", new ForgeLoadContext(context.getModEventBus()));

        Balm.registerModule(new ForgeCommonCapabilities());
        ((ForgeBalmRuntime) Balm.getRuntime()).initializeRuntime();

        Balm.getConfig().registerConfig(ExampleDeclarativeConfig.schema);
        Balm.getConfig().registerConfig(ExampleReflectionConfig.class);

        final var modEventBus = context.getModEventBus();
        ForgeBalmWorldGen.initializeBalmBiomeModifiers(modEventBus);
        modEventBus.addListener(ForgeBalmClient::onInitializeClient);

        final var capabilities = (ForgeBalmCapabilities) Balm.getCapabilities();
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("forge", "item_handler"), IItemHandler.class, ForgeCapabilities.ITEM_HANDLER);
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("forge", "fluid_handler"), IFluidHandler.class, ForgeCapabilities.FLUID_HANDLER);
        capabilities.addExistingType(ResourceLocation.fromNamespaceAndPath("forge", "energy_storage"), IEnergyStorage.class, ForgeCapabilities.ENERGY);
    }
}
