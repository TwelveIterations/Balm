package net.blay09.mods.balm.api.energy;

import net.minecraft.core.Direction;

public interface BalmEnergyStorageProvider {
    EnergyStorage getEnergyStorage();

    default EnergyStorage getEnergyStorage(Direction side) {
        return getEnergyStorage();
    }
}
