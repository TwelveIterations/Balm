package net.blay09.mods.balm.neoforge.platform.compatibility.energy.internal;

import net.blay09.mods.balm.platform.energy.EnergyStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public final class NeoForgeEnergyStorage implements EnergyHandler {
    private final EnergyStorage energyStorage;
    private final EnergyJournal energyJournal = new EnergyJournal();

    public NeoForgeEnergyStorage(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

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
        energyJournal.updateSnapshots(context);
        return energyStorage.fill(amount, false);
    }

    @Override
    public int extract(int amount, TransactionContext context) {
        energyJournal.updateSnapshots(context);
        return energyStorage.drain(amount, false);
    }

    private class EnergyJournal extends SnapshotJournal<Integer> {
        protected Integer createSnapshot() {
            return NeoForgeEnergyStorage.this.energyStorage.getEnergy();
        }

        protected void revertToSnapshot(Integer snapshot) {
            NeoForgeEnergyStorage.this.energyStorage.setEnergy(snapshot);
        }
    }
}
