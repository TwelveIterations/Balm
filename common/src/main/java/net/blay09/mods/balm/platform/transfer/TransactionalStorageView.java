package net.blay09.mods.balm.platform.transfer;

public interface TransactionalStorageView<R extends TransactionalResource> {
    R resource();

    long amount();

    long capacity();

    long extract(R resource, long maxAmount, TransactionContext transaction);
}
