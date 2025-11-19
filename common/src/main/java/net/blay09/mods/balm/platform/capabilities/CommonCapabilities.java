package net.blay09.mods.balm.platform.capabilities;

import net.blay09.mods.balm.platform.energy.EnergyStorage;
import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;

public class CommonCapabilities {
    public static CapabilityType<Block, Container, Direction> CONTAINER;
    public static CapabilityType<Block, FluidTank, Direction> FLUID_TANK;
    public static CapabilityType<Block, EnergyStorage, Direction> ENERGY_STORAGE;
}
