package net.blay09.mods.balm.fabric.platform.transfer.internal;

import net.blay09.mods.balm.platform.transfer.*;
import java.util.Iterator;
import java.util.function.Function;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;

public record FabricTransactionalStorage<R extends TransactionalResource, N>(Storage<N> storage, Function<R, N> toNative, Function<N, R> fromNative) implements TransactionalStorage<R> {
    @Override
    public long insert(R resource, long maxAmount, TransactionContext transaction) {
        return storage.insert(toNative.apply(resource), maxAmount, FabricTransactionContext.unwrap(transaction));
    }

    @Override
    public long extract(R resource, long maxAmount, TransactionContext transaction) {
        return storage.extract(toNative.apply(resource), maxAmount, FabricTransactionContext.unwrap(transaction));
    }

    @Override
    public Iterator<TransactionalStorageView<R>> iterator() {
        final var iterator = storage.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() { return iterator.hasNext(); }

            @Override
            public TransactionalStorageView<R> next() {
                final var view = iterator.next();
                return new TransactionalStorageView<>() {
                    @Override
                    public R resource() { return fromNative.apply(view.getResource()); }

                    @Override
                    public long amount() { return view.getAmount(); }

                    @Override
                    public long capacity() { return view.getCapacity(); }

                    @Override
                    public long extract(R resource, long maxAmount, TransactionContext transaction) {
                        return view.extract(toNative.apply(resource), maxAmount, FabricTransactionContext.unwrap(transaction));
                    }
                };
            }
        };
    }
}
