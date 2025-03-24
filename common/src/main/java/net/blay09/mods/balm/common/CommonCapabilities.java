package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityType;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;

public class CommonCapabilities {
    public static CapabilityType<Block, Container, Direction> CONTAINER;
    public static CapabilityType<Block, FluidTank, Direction> FLUID_TANK;
    public static CapabilityType<Block, EnergyStorage, Direction> ENERGY_STORAGE;

    public static void initialize(BalmCapabilities capabilities) {
        CONTAINER = capabilities.registerType(id("container"), Block.class, Container.class, Direction.class);
        FLUID_TANK = capabilities.registerType(id("fluid_tank"), Block.class, FluidTank.class, Direction.class);
        ENERGY_STORAGE = capabilities.registerType(id("energy_storage"), Block.class, EnergyStorage.class, Direction.class);

        capabilities.registerFallbackBlockEntityProvider(id("container"), CONTAINER, ((blockEntity, direction) -> {
            if (blockEntity instanceof BalmContainerProvider provider) {
                if (direction != null) {
                    return provider.getContainer(direction);
                } else {
                    return provider.getContainer();
                }
            } else if (blockEntity instanceof Container container) {
                return container;
            }
            return null;
        }));
        capabilities.registerFallbackBlockEntityProvider(id("fluid_tank"), FLUID_TANK, ((blockEntity, direction) -> {
            if (blockEntity instanceof BalmFluidTankProvider provider) {
                if (direction != null) {
                    return provider.getFluidTank(direction);
                } else {
                    return provider.getFluidTank();
                }
            }
            return null;
        }));
        capabilities.registerFallbackBlockEntityProvider(id("energy_storage"), ENERGY_STORAGE, ((blockEntity, direction) -> {
            if (blockEntity instanceof BalmEnergyStorageProvider provider) {
                if (direction != null) {
                    return provider.getEnergyStorage(direction);
                } else {
                    return provider.getEnergyStorage();
                }
            }
            return null;
        }));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("balm", path);
    }
}
