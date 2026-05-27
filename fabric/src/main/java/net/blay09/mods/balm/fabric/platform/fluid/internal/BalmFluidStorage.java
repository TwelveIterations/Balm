package net.blay09.mods.balm.fabric.platform.fluid.internal;

import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

public class BalmFluidStorage implements SlottedStorage<FluidVariant> {
    private final FluidTank fluidTank;
    private final List<BalmSingleFluidStorage> slots;

    public BalmFluidStorage(FluidTank fluidTank) {
        this.fluidTank = fluidTank;
        this.slots = IntStream.range(0, fluidTank.getSlotCount())
                .mapToObj(slot -> new BalmSingleFluidStorage(fluidTank, slot))
                .toList();
    }

    @Override
    public int getSlotCount() {
        return fluidTank.getSlotCount();
    }

    @Override
    public SingleSlotStorage<FluidVariant> getSlot(int slot) {
        return slots.get(slot);
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        return StorageUtil.insertStacking(slots, resource, maxAmount, transaction);
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        long extracted = 0;
        for (final var slot : slots) {
            if (extracted >= maxAmount) break;
            extracted += slot.extract(resource, maxAmount - extracted, transaction);
        }

        return extracted;
    }

    @Override
    public Iterator<StorageView<FluidVariant>> iterator() {
        return slots.stream()
                .map(slot -> (StorageView<FluidVariant>) slot)
                .iterator();
    }

    @Override
    public String toString() {
        return "BalmFluidStorage[" + fluidTank + "]";
    }
}
