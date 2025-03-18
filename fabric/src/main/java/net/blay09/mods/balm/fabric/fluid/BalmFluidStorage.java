package net.blay09.mods.balm.fabric.fluid;

import com.google.common.primitives.Ints;
import net.blay09.mods.balm.api.fluid.DefaultFluidTank;
import net.blay09.mods.balm.api.fluid.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public class BalmFluidStorage extends SnapshotParticipant<ResourceAmount<FluidVariant>> implements SingleSlotStorage<FluidVariant> {

    private final FluidTank fluidTank;

    public BalmFluidStorage(FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Override
    public long insert(FluidVariant fluidVariant, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(fluidVariant, maxAmount);

        if (getAmount() == 0) {
            updateSnapshots(transaction);
            return fluidTank.fill(fluidVariant.getFluid(), Ints.saturatedCast(maxAmount), false);
        }

        if (fluidVariant.isOf(getResource().getFluid())) {
            // Otherwise we can only accept the same fluid as the current one.
            long amountInserted = Math.min(maxAmount, getCapacity() - getAmount());
            updateSnapshots(transaction);
            return fluidTank.fill(fluidVariant.getFluid(), Ints.saturatedCast(amountInserted), false);
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
            return fluidTank.drain(fluidVariant.getFluid(), Ints.saturatedCast(amountExtracted), false);
        }

        return 0;
    }

    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    @Override
    public FluidVariant getResource() {
        return FluidVariant.of(fluidTank.getFluid());
    }

    @Override
    public long getAmount() {
        return fluidTank.getAmount();
    }

    @Override
    public long getCapacity() {
        return fluidTank.getCapacity();
    }

    @Override
    protected ResourceAmount<FluidVariant> createSnapshot() {
        return new ResourceAmount<>(getResource(), getAmount());
    }

    @Override
    protected void readSnapshot(ResourceAmount<FluidVariant> snapshot) {
        fluidTank.setFluid(snapshot.resource().getFluid(), Ints.saturatedCast(snapshot.amount()));
    }

    @Override
    public void onFinalCommit() {
        if (fluidTank instanceof DefaultFluidTank defaultFluidTank) {
            defaultFluidTank.setChanged();
        }
    }

    @Override
    public String toString() {
        return "BalmFluidStorage[" + fluidTank + "]";
    }
}
