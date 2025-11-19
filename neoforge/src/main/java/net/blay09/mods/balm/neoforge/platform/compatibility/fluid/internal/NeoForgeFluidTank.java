package net.blay09.mods.balm.neoforge.platform.compatibility.fluid.internal;

import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

public final class NeoForgeFluidTank implements ResourceHandler<FluidResource> {
    private final FluidTank fluidTank;
    private final FluidJournal fluidJournal = new FluidJournal();

    public NeoForgeFluidTank(FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public FluidResource getResource(int i) {
        return i == 0 ? FluidResource.of(fluidTank.getFluid()) : FluidResource.EMPTY;
    }

    @Override
    public long getAmountAsLong(int i) {
        return i == 0 ? fluidTank.getAmount() : 0;
    }

    @Override
    public long getCapacityAsLong(int i, FluidResource fluidResource) {
        return i == 0 ? fluidTank.getCapacity() : 0;
    }

    @Override
    public boolean isValid(int i, FluidResource fluidResource) {
        return i == 0 && (fluidTank.isEmpty() || fluidResource.is(fluidTank.getFluid()));
    }

    @Override
    public int insert(int i, FluidResource fluidResource, int amount, TransactionContext transaction) {
        fluidJournal.updateSnapshots(transaction);
        return i == 0 ? fluidTank.fill(fluidResource.getFluid(), amount, false) : amount;
    }

    @Override
    public int extract(int i, FluidResource fluidResource, int amount, TransactionContext transaction) {
        fluidJournal.updateSnapshots(transaction);
        return i == 0 ? fluidTank.drain(fluidResource.getFluid(), amount, false) : amount;
    }

    private class FluidJournal extends SnapshotJournal<FluidStack> {
        @Override
        protected FluidStack createSnapshot() {
            return new FluidStack(NeoForgeFluidTank.this.fluidTank.getFluid(), NeoForgeFluidTank.this.fluidTank.getAmount());
        }

        @Override
        protected void revertToSnapshot(@Nullable FluidStack snapshot) {
            if (snapshot != null) {
                NeoForgeFluidTank.this.fluidTank.setFluid(snapshot.getFluid(), snapshot.getAmount());
            }
        }
    }
}
