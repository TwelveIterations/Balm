package net.blay09.mods.balm.platform.transfer;

import net.blay09.mods.balm.Balm;

public interface TransactionalEnergyStorage {
    static TransactionalEnergyStorage energy(long capacity) {
        return energy(capacity, () -> {});
    }

    static TransactionalEnergyStorage energy(long capacity, Runnable onChange) {
        return Balm.modSupport().transactionalStorage().createEnergyStorage(capacity, onChange);
    }

    long insert(long maxAmount, TransactionContext transaction);

    long extract(long maxAmount, TransactionContext transaction);

    long getAmount();

    long getCapacity();
}
