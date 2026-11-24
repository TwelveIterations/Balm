package net.blay09.mods.balm.forge.platform.energy.internal;

import com.google.common.primitives.Ints;
import net.blay09.mods.balm.platform.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgeEnergyStorage implements IEnergyStorage {

    private final EnergyStorage energyStorage;

    public ForgeEnergyStorage(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return Ints.saturatedCast(energyStorage.fill(maxReceive, simulate));
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return Ints.saturatedCast(energyStorage.drain(maxExtract, simulate));
    }

    @Override
    public int getEnergyStored() {
        return Ints.saturatedCast(energyStorage.getEnergy());
    }

    @Override
    public int getMaxEnergyStored() {
        return Ints.saturatedCast(energyStorage.getCapacity());
    }

    @Override
    public boolean canExtract() {
        return energyStorage.canDrain();
    }

    @Override
    public boolean canReceive() {
        return energyStorage.canFill();
    }
}
