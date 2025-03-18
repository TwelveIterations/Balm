package net.blay09.mods.balm.api.capability;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;

public class CapabilityTypes {
    public static CapabilityType<Container, Direction> CONTAINER = Balm.getCapabilities().getType(id("container"), Container.class, Direction.class);
    public static CapabilityType<FluidTank, Direction> FLUID_TANK = Balm.getCapabilities().getType(id("fluid_tank"), FluidTank.class, Direction.class);
    public static CapabilityType<EnergyStorage, Direction> ENERGY_STORAGE = Balm.getCapabilities()
            .getType(id("energy_storage"), EnergyStorage.class, Direction.class);

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("balm", path);
    }
}
