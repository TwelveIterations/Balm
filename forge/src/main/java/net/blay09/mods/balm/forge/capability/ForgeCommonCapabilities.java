package net.blay09.mods.balm.forge.capability;

import net.blay09.mods.balm.platform.capabilities.BalmCapabilities;
import net.blay09.mods.balm.platform.energy.EnergyStorage;
import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.blay09.mods.balm.platform.module.BalmModule;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ForgeCommonCapabilities implements BalmModule {
    @Override
    public Identifier getId() {
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

    private static Identifier id(String container) {
        return Identifier.fromNamespaceAndPath("balm", container);
    }
}
