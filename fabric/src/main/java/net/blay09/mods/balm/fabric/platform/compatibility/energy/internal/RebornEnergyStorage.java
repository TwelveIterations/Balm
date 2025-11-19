package net.blay09.mods.balm.fabric.platform.compatibility.energy.internal;

import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

public class RebornEnergyStorage extends SnapshotParticipant<Long> implements EnergyStorage {

    private final net.blay09.mods.balm.platform.energy.EnergyStorage energyStorage;

    public RebornEnergyStorage(net.blay09.mods.balm.platform.energy.EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notNegative(maxAmount);
        updateSnapshots(transaction);
        return energyStorage.fill((int) maxAmount, false);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notNegative(maxAmount);
        updateSnapshots(transaction);
        return energyStorage.drain((int) maxAmount, false);
    }

    @Override
    public long getAmount() {
        return energyStorage.getEnergy();
    }

    @Override
    public long getCapacity() {
        return energyStorage.getCapacity();
    }

    @Override
    protected Long createSnapshot() {
        return (long) energyStorage.getEnergy();
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        energyStorage.setEnergy(snapshot.intValue());
    }
}
