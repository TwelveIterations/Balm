package net.blay09.mods.balm.platform.energy;

import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

public interface BalmEnergyStorageProvider {
    @Nullable
    EnergyStorage getEnergyStorage();

    @Nullable
    default EnergyStorage getEnergyStorage(Direction side) {
        return getEnergyStorage();
    }
}
