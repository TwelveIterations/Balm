package net.blay09.mods.balm.platform.capabilities;

import net.blay09.mods.balm.platform.energy.EnergyStorage;
import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

public class CommonCapabilities {
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static CapabilityType<Block, Container, Direction> CONTAINER;
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static CapabilityType<Block, FluidTank, Direction> FLUID_TANK;
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static CapabilityType<Block, EnergyStorage, Direction> ENERGY_STORAGE;
}
