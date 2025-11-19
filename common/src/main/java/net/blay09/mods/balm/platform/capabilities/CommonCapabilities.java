package net.blay09.mods.balm.platform.capabilities;

import net.blay09.mods.balm.platform.energy.EnergyStorage;
import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

public class CommonCapabilities {
    @Nullable
    public static CapabilityType<Block, Container, Direction> CONTAINER;
    @Nullable
    public static CapabilityType<Block, FluidTank, Direction> FLUID_TANK;
    @Nullable
    public static CapabilityType<Block, EnergyStorage, Direction> ENERGY_STORAGE;
}
