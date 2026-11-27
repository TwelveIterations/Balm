package net.blay09.mods.balm.platform.capabilities;

import net.blay09.mods.balm.platform.energy.EnergyStorage;
import net.blay09.mods.balm.platform.transfer.TransactionalEnergyStorage;
import net.blay09.mods.balm.platform.transfer.TransactionalStorage;
import net.blay09.mods.balm.platform.transfer.TransactionalItem;
import net.blay09.mods.balm.platform.transfer.TransactionalFluid;
import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;

public class CommonCapabilities {
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static CapabilityType<Block, TransactionalStorage<TransactionalItem>, Direction> ITEM_STORAGE;
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static CapabilityType<Block, TransactionalStorage<TransactionalFluid>, Direction> FLUID_STORAGE;
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static CapabilityType<Block, TransactionalEnergyStorage, Direction> TRANSACTIONAL_ENERGY_STORAGE;
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static CapabilityType<Block, Container, Direction> CONTAINER;
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static CapabilityType<Block, FluidTank, Direction> FLUID_TANK;
    @SuppressWarnings("NotNullFieldNotInitialized")
    public static CapabilityType<Block, EnergyStorage, Direction> ENERGY_STORAGE;
}
