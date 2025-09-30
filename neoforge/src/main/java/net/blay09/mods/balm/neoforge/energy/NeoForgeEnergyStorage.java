package net.blay09.mods.balm.neoforge.energy;

import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public record NeoForgeEnergyStorage(EnergyStorage energyStorage) implements EnergyHandler {

    @Override
    public long getAmountAsLong() {
        return energyStorage.getEnergy();
    }

    @Override
    public long getCapacityAsLong() {
        return energyStorage.getCapacity();
    }

    @Override
    public int insert(int amount, TransactionContext context) {
        return energyStorage.fill(amount, false);
    }

    @Override
    public int extract(int amount, TransactionContext context) {
        return energyStorage.drain(amount, false);
    }
}
