package net.blay09.mods.balm.neoforge.platform.compatibility.fluid.internal;

import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

public final class NeoForgeFluidTank implements ResourceHandler<FluidResource> {
    private final FluidTank fluidTank;
    private final FluidJournal fluidJournal = new FluidJournal();

    public NeoForgeFluidTank(FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Override
    public int size() {
        return fluidTank.getSlotCount();
    }

    @Override
    public FluidResource getResource(int i) {
        if (i < 0 || i >= fluidTank.getSlotCount()) {
            return FluidResource.EMPTY;
        }

        return FluidResource.of(fluidTank.getFluid(i));
    }

    @Override
    public long getAmountAsLong(int i) {
        if (i < 0 || i >= fluidTank.getSlotCount()) {
            return 0;
        }

        return fluidTank.getAmount(i);
    }

    @Override
    public long getCapacityAsLong(int i, FluidResource fluidResource) {
        if (i < 0 || i >= fluidTank.getSlotCount()) {
            return 0;
        }

        return fluidTank.getCapacity(i);
    }

    @Override
    public boolean isValid(int i, FluidResource fluidResource) {
        if (i < 0 || i >= fluidTank.getSlotCount()) {
            return false;
        }

        return fluidTank.isEmpty(i) || fluidResource.is(fluidTank.getFluid(i));
    }

    @Override
    public int insert(int i, FluidResource fluidResource, int amount, TransactionContext transaction) {
        if (i < 0 || i >= fluidTank.getSlotCount()) {
            return 0;
        }

        fluidJournal.updateSnapshots(transaction);
        return fluidTank.fill(i, fluidResource.getFluid(), amount, false);
    }

    @Override
    public int extract(int i, FluidResource fluidResource, int amount, TransactionContext transaction) {
        if (i < 0 || i >= fluidTank.getSlotCount()) {
            return 0;
        }

        fluidJournal.updateSnapshots(transaction);
        return fluidTank.drain(i, fluidResource.getFluid(), amount, false);
    }

    private class FluidJournal extends SnapshotJournal<List<FluidStack>> {
        @Override
        protected List<FluidStack> createSnapshot() {
            return IntStream.range(0, NeoForgeFluidTank.this.fluidTank.getSlotCount())
                    .mapToObj(slot -> new FluidStack(NeoForgeFluidTank.this.fluidTank.getFluid(slot), NeoForgeFluidTank.this.fluidTank.getAmount(slot)))
                    .toList();
        }

        @Override
        protected void revertToSnapshot(@Nullable List<FluidStack> snapshot) {
            if (snapshot != null) {
                for (int i = 0; i < snapshot.size(); i++) {
                    final var fluidStack = snapshot.get(i);
                    NeoForgeFluidTank.this.fluidTank.setFluid(i, fluidStack.getFluid(), fluidStack.getAmount());
                }
            }
        }
    }
}
