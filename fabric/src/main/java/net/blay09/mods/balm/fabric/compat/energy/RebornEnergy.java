package net.blay09.mods.balm.fabric.compat.energy;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.capability.CapabilityTypes;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import team.reborn.energy.api.EnergyStorage;

public class RebornEnergy {
    public RebornEnergy() {
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, direction) -> {
            if (blockEntity instanceof BalmEnergyStorageProvider energyStorageProvider) {
                final var energyStorage = direction != null ? energyStorageProvider.getEnergyStorage(direction) : energyStorageProvider.getEnergyStorage();
                if (energyStorage != null) {
                    return new RebornEnergyStorage(energyStorage);
                }
            } else {
                final var energyStorage = Balm.getCapabilities().getCapability(blockEntity, direction, CapabilityTypes.ENERGY_STORAGE);
                if (energyStorage != null) {
                    return new RebornEnergyStorage(energyStorage);
                }
            }

            return null;
        });
    }
}
