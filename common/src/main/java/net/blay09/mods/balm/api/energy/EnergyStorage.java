package net.blay09.mods.balm.api.energy;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;

public class EnergyStorage {

    private final int capacity;
    private final int maxFill;
    private final int maxDrain;
    private int energy;

    public EnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public EnergyStorage(int capacity, int maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public EnergyStorage(int capacity, int maxFill, int maxDrain) {
        this(maxDrain, capacity, maxFill, 0);
    }

    public EnergyStorage(int maxDrain, int capacity, int maxFill, int amount) {
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
        this.energy = Math.max(0, Math.min(capacity, amount));
    }

    public int fill(int maxFill, boolean simulate) {
        if (!canFill()) {
            return 0;
        }

        int filled = Math.min(capacity - energy, Math.min(this.maxFill, maxFill));
        if (!simulate) {
            energy += filled;
        }
        return filled;
    }

    public int drain(int maxDrain, boolean simulate) {
        if (!canDrain()) {
            return 0;
        }

        int drained = Math.min(energy, Math.min(this.maxDrain, maxDrain));
        if (!simulate) {
            energy -= drained;
        }
        return drained;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean canDrain() {
        return maxDrain > 0;
    }

    public boolean canFill() {
        return maxFill > 0;
    }

    public IntTag serialize() {
        return IntTag.valueOf(energy);
    }

    @Deprecated
    public void deserialize(IntTag tag) {
        energy = tag.getAsInt();
    }

    public void deserialize(Tag tag) {
        energy = ((IntTag) tag).getAsInt();
    }
}
