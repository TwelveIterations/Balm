package net.blay09.mods.balm.platform.energy;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class DefaultEnergyStorage implements EnergyStorage {

    private final long capacity;
    private final long maxFill;
    private final long maxDrain;
    private long energy;

    public DefaultEnergyStorage(long capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public DefaultEnergyStorage(long capacity, long maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public DefaultEnergyStorage(long capacity, long maxFill, long maxDrain) {
        this(maxDrain, capacity, maxFill, 0);
    }

    public DefaultEnergyStorage(long maxDrain, long capacity, long maxFill, long amount) {
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
        this.energy = Math.max(0, Math.min(capacity, amount));
    }

    public long fill(long maxFill, boolean simulate) {
        if (!canFill()) {
            return 0;
        }

        long filled = Math.min(capacity - energy, Math.min(this.maxFill, maxFill));
        if (!simulate) {
            energy += filled;
            setChanged();
        }
        return filled;
    }

    public long drain(long maxDrain, boolean simulate) {
        if (!canDrain()) {
            return 0;
        }

        long drained = Math.min(energy, Math.min(this.maxDrain, maxDrain));
        if (!simulate) {
            energy -= drained;
            setChanged();
        }
        return drained;
    }

    public long getEnergy() {
        return energy;
    }

    public void setEnergy(long energy) {
        this.energy = energy;
        setChanged();
    }

    public long getCapacity() {
        return capacity;
    }

    public boolean canDrain() {
        return maxDrain > 0;
    }

    public boolean canFill() {
        return maxFill > 0;
    }

    public void serialize(ValueOutput output) {
        output.putLong("Energy", energy);
    }

    public void deserialize(ValueInput input) {
        energy = input.getLongOr("Energy", 0L);
    }

    public void setChanged() {
    }
}
