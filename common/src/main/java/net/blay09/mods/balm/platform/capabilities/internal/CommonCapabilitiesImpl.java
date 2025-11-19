package net.blay09.mods.balm.platform.capabilities.internal;

import net.blay09.mods.balm.world.BalmContainerProvider;
import net.blay09.mods.balm.platform.capabilities.BalmCapabilities;
import net.blay09.mods.balm.platform.capabilities.CommonCapabilities;
import net.blay09.mods.balm.platform.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.platform.energy.EnergyStorage;
import net.blay09.mods.balm.platform.fluid.BalmFluidTankProvider;
import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Block;

public class CommonCapabilitiesImpl {

    public static void registerCapabilities(BalmCapabilities capabilities) {
        CommonCapabilities.CONTAINER = capabilities.registerType(id("container"), Block.class, Container.class, Direction.class);
        CommonCapabilities.FLUID_TANK = capabilities.registerType(id("fluid_tank"), Block.class, FluidTank.class, Direction.class);
        CommonCapabilities.ENERGY_STORAGE = capabilities.registerType(id("energy_storage"), Block.class, EnergyStorage.class, Direction.class);

        capabilities.registerFallbackBlockEntityProvider(id("container"), CommonCapabilities.CONTAINER, ((blockEntity, direction) -> {
            if (blockEntity instanceof BalmContainerProvider provider) {
                if (direction != null) {
                    return provider.getContainer(direction);
                } else {
                    return provider.getContainer();
                }
            }
            return null;
        }));
        capabilities.registerFallbackBlockEntityProvider(id("fluid_tank"), CommonCapabilities.FLUID_TANK, ((blockEntity, direction) -> {
            if (blockEntity instanceof BalmFluidTankProvider provider) {
                if (direction != null) {
                    return provider.getFluidTank(direction);
                } else {
                    return provider.getFluidTank();
                }
            }
            return null;
        }));
        capabilities.registerFallbackBlockEntityProvider(id("energy_storage"), CommonCapabilities.ENERGY_STORAGE, ((blockEntity, direction) -> {
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

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath("balm", path);
    }
}
