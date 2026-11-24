package net.blay09.mods.balm.platform.transfer;

import net.blay09.mods.balm.platform.fluid.FluidAmount;
import net.blay09.mods.balm.Balm;

public interface TransactionalStorage<R extends TransactionalResource> extends Iterable<TransactionalStorageView<R>> {
    static TransactionalStorage<TransactionalItem> items(int slots) {
        return items(slots, () -> {});
    }

    static TransactionalStorage<TransactionalItem> items(int slots, Runnable onChange) {
        return Balm.modSupport().transactionalStorage().createItemStorage(slots, onChange);
    }

    static TransactionalStorage<TransactionalFluid> fluids(int tanks, FluidAmount capacity) {
        return fluids(tanks, capacity, () -> {});
    }

    static TransactionalStorage<TransactionalFluid> fluids(int tanks, FluidAmount capacity, Runnable onChange) {
        return Balm.modSupport().transactionalStorage().createFluidStorage(tanks, capacity, onChange);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static <R extends TransactionalResource> Class<TransactionalStorage<R>> asClass() {
        return (Class) TransactionalStorage.class;
    }

    long insert(R resource, long maxAmount, TransactionContext transaction);

    long extract(R resource, long maxAmount, TransactionContext transaction);
}
