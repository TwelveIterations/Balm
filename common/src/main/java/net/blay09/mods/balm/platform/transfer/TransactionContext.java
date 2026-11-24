package net.blay09.mods.balm.platform.transfer;

import net.blay09.mods.balm.Balm;

public interface TransactionContext extends AutoCloseable {
    static TransactionContext open() {
        return Balm.modSupport().transactionalStorage().openTransaction();
    }

    TransactionContext openNested();

    void commit();

    @Override
    void close();
}
