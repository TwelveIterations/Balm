package net.blay09.mods.balm.platform.compatibility.transactionalstorage;

import net.blay09.mods.balm.platform.fluid.FluidAmount;
import net.blay09.mods.balm.platform.transfer.TransactionalFluid;
import net.blay09.mods.balm.platform.transfer.TransactionalItem;
import net.blay09.mods.balm.platform.transfer.TransactionContext;
import net.blay09.mods.balm.platform.transfer.TransactionalStorage;
import net.blay09.mods.balm.platform.transfer.TransactionalEnergyStorage;

public interface BalmModSupportTransactionalStorage {
    TransactionContext openTransaction();

    TransactionalStorage<TransactionalItem> createItemStorage(int slots, Runnable onChange);

    TransactionalStorage<TransactionalFluid> createFluidStorage(int tanks, FluidAmount capacity, Runnable onChange);

    TransactionalEnergyStorage createEnergyStorage(long capacity, Runnable onChange);
}
