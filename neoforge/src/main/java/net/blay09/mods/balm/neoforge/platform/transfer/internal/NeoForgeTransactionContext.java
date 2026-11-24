package net.blay09.mods.balm.neoforge.platform.transfer.internal;

import net.blay09.mods.balm.platform.transfer.TransactionContext;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public record NeoForgeTransactionContext(Transaction transaction) implements TransactionContext {
    @Override
    public TransactionContext openNested() {
        return new NeoForgeTransactionContext(Transaction.open(transaction));
    }

    @Override
    public void commit() { transaction.commit(); }

    @Override
    public void close() { transaction.close(); }

    static Transaction unwrap(TransactionContext transaction) {
        if (!(transaction instanceof NeoForgeTransactionContext(Transaction unwrapped))) {
            throw new IllegalArgumentException("Expected a NeoForgeTransactionContext instance");
        }
        return unwrapped;
    }
}
