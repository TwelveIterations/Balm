package net.blay09.mods.balm.forge.capability;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.blay09.mods.balm.api.module.BalmModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ForgeCommonCapabilities implements BalmModule {
    @Override
    public ResourceLocation getId() {
        return id("forge_common_capabilities");
    }

    @Override
    public void registerCapabilities(BalmCapabilities capabilities) {
        final var forgeCapabilities = (ForgeBalmCapabilities) capabilities;
        forgeCapabilities.preRegisterType(id("container"), CapabilityManager.get(new CapabilityToken<Container>() {
        }));
        forgeCapabilities.preRegisterType(id("fluid_tank"), CapabilityManager.get(new CapabilityToken<FluidTank>() {
        }));
        forgeCapabilities.preRegisterType(id("energy_storage"), CapabilityManager.get(new CapabilityToken<EnergyStorage>() {
        }));
    }

    private static ResourceLocation id(String container) {
        return ResourceLocation.fromNamespaceAndPath("balm", container);
    }
}
