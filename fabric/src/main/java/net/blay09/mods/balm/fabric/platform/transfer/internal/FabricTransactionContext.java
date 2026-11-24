package net.blay09.mods.balm.fabric.platform.transfer.internal;

import net.blay09.mods.balm.platform.transfer.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public record FabricTransactionContext(Transaction transaction) implements TransactionContext {
    @Override
    public TransactionContext openNested() {
        return new FabricTransactionContext(transaction.openNested());
    }

    @Override
    public void commit() { transaction.commit(); }

    @Override
    public void close() { transaction.close(); }

    static Transaction unwrap(TransactionContext transaction) {
        if (!(transaction instanceof FabricTransactionContext(Transaction unwrapped))) {
            throw new IllegalArgumentException("Expected a FabricTransactionContext instance");
        }
        return unwrapped;
    }
}
