package net.blay09.mods.balm.neoforge.platform.transfer.internal;

import net.blay09.mods.balm.platform.transfer.TransactionContext;
import net.blay09.mods.balm.platform.transfer.TransactionalEnergyStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;

public record NeoForgeTransactionalEnergyStorage(EnergyHandler storage) implements TransactionalEnergyStorage {
    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        return storage.insert((int) Math.min(maxAmount, Integer.MAX_VALUE), NeoForgeTransactionContext.unwrap(transaction));
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        return storage.extract((int) Math.min(maxAmount, Integer.MAX_VALUE), NeoForgeTransactionContext.unwrap(transaction));
    }

    @Override
    public long getAmount() { return storage.getAmountAsLong(); }

    @Override
    public long getCapacity() { return storage.getCapacityAsLong(); }

    public static EnergyHandler unwrap(TransactionalEnergyStorage storage) {
        if (!(storage instanceof NeoForgeTransactionalEnergyStorage(EnergyHandler unwrapped))) {
            throw new IllegalArgumentException("Expected a NeoForgeTransactionalEnergyStorage instance");
        }
        return unwrapped;
    }
}
