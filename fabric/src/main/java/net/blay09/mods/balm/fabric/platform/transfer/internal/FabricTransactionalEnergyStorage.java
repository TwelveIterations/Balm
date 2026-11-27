package net.blay09.mods.balm.fabric.platform.transfer.internal;

import net.blay09.mods.balm.platform.transfer.TransactionContext;
import net.blay09.mods.balm.platform.transfer.TransactionalEnergyStorage;
import team.reborn.energy.api.EnergyStorage;

public record FabricTransactionalEnergyStorage(EnergyStorage storage) implements TransactionalEnergyStorage {
    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        return storage.insert(maxAmount, FabricTransactionContext.unwrap(transaction));
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        return storage.extract(maxAmount, FabricTransactionContext.unwrap(transaction));
    }

    @Override
    public long getAmount() { return storage.getAmount(); }

    @Override
    public long getCapacity() { return storage.getCapacity(); }

    public static EnergyStorage unwrap(TransactionalEnergyStorage storage) {
        if (!(storage instanceof FabricTransactionalEnergyStorage(EnergyStorage unwrapped))) {
            throw new IllegalArgumentException("Expected a FabricTransactionalEnergyStorage instance");
        }
        return unwrapped;
    }
}
