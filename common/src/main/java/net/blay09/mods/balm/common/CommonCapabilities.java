package net.blay09.mods.balm.common;

import net.blay09.mods.balm.api.capability.BalmCapabilities;
import net.blay09.mods.balm.api.capability.CapabilityTypes;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.fluid.BalmFluidTankProvider;
import net.minecraft.world.Container;

public class CommonCapabilities {
    public static void initialize(BalmCapabilities capabilities) {
        capabilities.registerFallbackBlockEntityProvider(CapabilityTypes.CONTAINER, ((blockEntity, direction) -> {
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
        capabilities.registerFallbackBlockEntityProvider(CapabilityTypes.FLUID_TANK, ((blockEntity, direction) -> {
            if (blockEntity instanceof BalmFluidTankProvider provider) {
                if (direction != null) {
                    return provider.getFluidTank(direction);
                } else {
                    return provider.getFluidTank();
                }
            }
            return null;
        }));
        capabilities.registerFallbackBlockEntityProvider(CapabilityTypes.ENERGY_STORAGE, ((blockEntity, direction) -> {
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
}
