package net.blay09.mods.balm.fabric.platform.fluid.internal;

import com.google.common.primitives.Ints;
import net.blay09.mods.balm.platform.fluid.DefaultFluidTank;
import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public class BalmSingleFluidStorage extends SnapshotParticipant<ResourceAmount<FluidVariant>> implements SingleSlotStorage<FluidVariant> {

    private final FluidTank fluidTank;
    private final int slot;

    public BalmSingleFluidStorage(FluidTank fluidTank, int slot) {
        this.fluidTank = fluidTank;
        this.slot = slot;
    }

    @Override
    public long insert(FluidVariant fluidVariant, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(fluidVariant, maxAmount);

        if (getAmount() == 0) {
            updateSnapshots(transaction);
            return fluidTank.fill(slot, fluidVariant.getFluid(), Ints.saturatedCast(maxAmount), false);
        }

        if (fluidVariant.isOf(getResource().getFluid())) {
            // Otherwise we can only accept the same fluid as the current one.
            long amountInserted = Math.min(maxAmount, getCapacity() - getAmount());
            updateSnapshots(transaction);
            return fluidTank.fill(slot, fluidVariant.getFluid(), Ints.saturatedCast(amountInserted), false);
        } else {
            return 0;
        }
    }

    @Override
    public long extract(FluidVariant fluidVariant, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(fluidVariant, maxAmount);

        if (fluidVariant.isOf(getResource().getFluid())) {
            long currentLevel = getAmount();
            long amountExtracted = Math.min(maxAmount, currentLevel);
            return fluidTank.drain(slot, fluidVariant.getFluid(), Ints.saturatedCast(amountExtracted), false);
        }

        return 0;
    }

    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    @Override
    public FluidVariant getResource() {
        return FluidVariant.of(fluidTank.getFluid(slot));
    }

    @Override
    public long getAmount() {
        return fluidTank.getAmount(slot);
    }

    @Override
    public long getCapacity() {
        return fluidTank.getCapacity(slot);
    }

    @Override
    protected ResourceAmount<FluidVariant> createSnapshot() {
        return new ResourceAmount<>(getResource(), getAmount());
    }

    @Override
    protected void readSnapshot(ResourceAmount<FluidVariant> snapshot) {
        fluidTank.setFluid(slot, snapshot.resource().getFluid(), Ints.saturatedCast(snapshot.amount()));
    }

    @Override
    public void onFinalCommit() {
        if (fluidTank instanceof DefaultFluidTank defaultFluidTank) {
            defaultFluidTank.setChanged();
        }
    }

    @Override
    public String toString() {
        return "BalmSingleFluidStorage[" + fluidTank + "]";
    }
}
