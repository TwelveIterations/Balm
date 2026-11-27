package net.blay09.mods.balm.neoforge.platform.transfer.internal;

import net.blay09.mods.balm.platform.transfer.*;
import java.util.Iterator;
import java.util.function.Function;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import java.util.stream.IntStream;

public record NeoForgeTransactionalStorage<R extends TransactionalResource, N extends Resource>(ResourceHandler<N> storage, Function<R, N> toNative, Function<N, R> fromNative) implements TransactionalStorage<R> {
    @Override
    public long insert(R resource, long maxAmount, TransactionContext transaction) {
        return storage.insert(toNative.apply(resource), (int) Math.min(maxAmount, Integer.MAX_VALUE), NeoForgeTransactionContext.unwrap(transaction));
    }

    @Override
    public long extract(R resource, long maxAmount, TransactionContext transaction) {
        return storage.extract(toNative.apply(resource), (int) Math.min(maxAmount, Integer.MAX_VALUE), NeoForgeTransactionContext.unwrap(transaction));
    }

    @Override
    public Iterator<TransactionalStorageView<R>> iterator() {
        final var iterator = IntStream.range(0, storage.size()).boxed().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() { return iterator.hasNext(); }

            @Override
            public TransactionalStorageView<R> next() {
                final var view = iterator.next();
                return new TransactionalStorageView<>() {
                    @Override
                    public R resource() { return fromNative.apply(storage.getResource(view)); }

                    @Override
                    public long amount() { return storage.getAmountAsLong(view); }

                    @Override
                    public long capacity() { return storage.getCapacityAsLong(view, storage.getResource(view)); }

                    @Override
                    public long extract(R resource, long maxAmount, TransactionContext transaction) {
                        return storage.extract(view, toNative.apply(resource), (int) Math.min(maxAmount, Integer.MAX_VALUE), NeoForgeTransactionContext.unwrap(transaction));
                    }
                };
            }
        };
    }
}
