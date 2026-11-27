package net.blay09.mods.balm.neoforge.platform.compatibility.transactionalstorage.internal;

import net.blay09.mods.balm.neoforge.platform.transfer.internal.NeoForgeTransactionalEnergyStorage;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import net.blay09.mods.balm.platform.fluid.FluidAmount;
import net.blay09.mods.balm.neoforge.platform.transfer.internal.NeoForgeTransactionalStorage;
import net.blay09.mods.balm.neoforge.platform.transfer.internal.NeoForgeTransactionContext;
import net.blay09.mods.balm.platform.compatibility.transactionalstorage.BalmModSupportTransactionalStorage;
import net.blay09.mods.balm.platform.transfer.*;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class NeoForgeBalmModSupportTransactionalStorage implements BalmModSupportTransactionalStorage {
    @Override
    public TransactionContext openTransaction() {
        return new NeoForgeTransactionContext(Transaction.openRoot());
    }

    public static NeoForgeTransactionalStorage<TransactionalItem, ItemResource> wrapItems(ResourceHandler<ItemResource> storage) {
        return new NeoForgeTransactionalStorage<>(storage,
                resource -> ItemResource.of(resource.item(), resource.components()),
                resource -> new TransactionalItem(resource.getItem(), resource.getComponentsPatch()));
    }

    public static NeoForgeTransactionalStorage<TransactionalFluid, FluidResource> wrapFluids(ResourceHandler<FluidResource> storage) {
        return new NeoForgeTransactionalStorage<>(storage,
                resource -> FluidResource.of(resource.fluid(), resource.components()),
                resource -> new TransactionalFluid(resource.getFluid(), resource.getComponentsPatch()));
    }

    @Override
    public TransactionalStorage<TransactionalItem> createItemStorage(int slots, Runnable onChange) {
        return wrapItems(new ItemStacksResourceHandler(slots) {
            @Override
            protected void onContentsChanged(int index, ItemStack previousContents) { onChange.run(); }
        });
    }

    @Override
    public TransactionalStorage<TransactionalFluid> createFluidStorage(int tanks, FluidAmount capacity, Runnable onChange) {
        final var amount = Math.toIntExact(capacity.to(FluidAmount.Unit.MILLIBUCKETS).amount());
        return wrapFluids(new FluidStacksResourceHandler(tanks, amount) {
            @Override
            protected void onContentsChanged(int index, FluidStack previousContents) { onChange.run(); }
        });
    }

    @Override
    public TransactionalEnergyStorage createEnergyStorage(long capacity, Runnable onChange) {
        return new NeoForgeTransactionalEnergyStorage(
                new SimpleEnergyHandler(Math.toIntExact(capacity)) {
                    @Override
                    protected void onEnergyChanged(int previousAmount) { onChange.run(); }
                });
    }

    @SuppressWarnings("unchecked")
    public static <R extends TransactionalResource, N extends Resource> ResourceHandler<N> unwrap(TransactionalStorage<R> storage) {
        if (!(storage instanceof NeoForgeTransactionalStorage<?, ?> nativeStorage)) {
            throw new IllegalArgumentException("Expected a NeoForgeTransactionalStorage instance");
        }
        return (ResourceHandler<N>) nativeStorage.storage();
    }

}
