package net.blay09.mods.balm.fabric.platform.compatibility.transactionalstorage.internal;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.fabric.platform.compatibility.energy.internal.RebornEnergy;
import net.blay09.mods.balm.platform.fluid.FluidAmount;
import net.blay09.mods.balm.fabric.platform.transfer.internal.FabricTransactionalStorage;
import net.blay09.mods.balm.fabric.platform.transfer.internal.FabricTransactionContext;
import net.blay09.mods.balm.platform.compatibility.transactionalstorage.BalmModSupportTransactionalStorage;
import net.blay09.mods.balm.platform.transfer.*;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import java.util.stream.IntStream;

public class FabricBalmModSupportTransactionalStorage implements BalmModSupportTransactionalStorage {
    @Override
    public TransactionContext openTransaction() {
        return new FabricTransactionContext(Transaction.openOuter());
    }

    public static FabricTransactionalStorage<TransactionalItem, ItemVariant> wrapItems(Storage<ItemVariant> storage) {
        return new FabricTransactionalStorage<>(storage,
                resource -> ItemVariant.of(resource.item(), resource.components()),
                resource -> new TransactionalItem(resource.getItem(), resource.getComponentsPatch()));
    }

    public static FabricTransactionalStorage<TransactionalFluid, FluidVariant> wrapFluids(Storage<FluidVariant> storage) {
        return new FabricTransactionalStorage<>(storage,
                resource -> FluidVariant.of(resource.fluid(), resource.components()),
                resource -> new TransactionalFluid(resource.getFluid(), resource.getComponentsPatch()));
    }

    @Override
    public TransactionalStorage<TransactionalItem> createItemStorage(int slots, Runnable onChange) {
        final var storages = IntStream.range(0, slots).mapToObj(_ -> new SingleVariantStorage<ItemVariant>() {
            @Override
            protected void onFinalCommit() { onChange.run(); }

            @Override
            protected ItemVariant getBlankVariant() { return ItemVariant.blank(); }

            @Override
            protected long getCapacity(ItemVariant variant) { return variant.isBlank() ? 64 : variant.toStack().getMaxStackSize(); }
        }).toList();
        return wrapItems(new CombinedStorage<>(storages));
    }

    @Override
    public TransactionalStorage<TransactionalFluid> createFluidStorage(int tanks, FluidAmount capacity, Runnable onChange) {
        final var tankCapacity = capacity.to(FluidAmount.Unit.DROPLETS).amount();
        final var storages = IntStream.range(0, tanks).mapToObj(_ -> new SingleVariantStorage<FluidVariant>() {
            @Override
            protected void onFinalCommit() { onChange.run(); }

            @Override
            protected FluidVariant getBlankVariant() { return FluidVariant.blank(); }

            @Override
            protected long getCapacity(FluidVariant variant) { return tankCapacity; }
        }).toList();
        return wrapFluids(new CombinedStorage<>(storages));
    }

    @Override
    public TransactionalEnergyStorage createEnergyStorage(long capacity, Runnable onChange) {
        if (!Balm.platform().isModLoaded("team_reborn_energy")) {
            throw new UnsupportedOperationException("TransactionalEnergyStorage requires Team Reborn Energy to be installed");
        }
        return RebornEnergy.createStorage(capacity, onChange);
    }

    @SuppressWarnings("unchecked")
    public static <R extends TransactionalResource, N> Storage<N> unwrap(TransactionalStorage<R> storage) {
        if (!(storage instanceof FabricTransactionalStorage<?, ?> nativeStorage)) {
            throw new IllegalArgumentException("Provider must return storage backed by this platform's Transfer API");
        }
        return (Storage<N>) nativeStorage.storage();
    }
}
