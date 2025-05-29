package net.blay09.mods.balm.api.energy;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class DefaultEnergyStorage implements EnergyStorage {

    private final int capacity;
    private final int maxFill;
    private final int maxDrain;
    private int energy;

    public DefaultEnergyStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public DefaultEnergyStorage(int capacity, int maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public DefaultEnergyStorage(int capacity, int maxFill, int maxDrain) {
        this(maxDrain, capacity, maxFill, 0);
    }

    public DefaultEnergyStorage(int maxDrain, int capacity, int maxFill, int amount) {
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
            setChanged();
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
            setChanged();
        }
        return drained;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        setChanged();
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

    public void serialize(ValueOutput output) {
        output.putInt("Energy", energy);
    }

    public void deserialize(ValueInput input) {
        energy = input.getIntOr("Energy", 0);
    }

    /**
     * @deprecated Use {@link #serialize(ValueOutput)} instead. Note that the data format now uses an "Energy" key instead of returning an IntTag directly.
     */
    @Deprecated(forRemoval = true, since = "1.22")
    public IntTag serialize() {
        return IntTag.valueOf(energy);
    }

    /**
     * @deprecated Use {@link #deserialize(ValueInput)} instead. Note that the data format now uses an "Energy" key instead of returning an IntTag directly.
     */
    @Deprecated(forRemoval = true, since = "1.22")
    public void deserialize(Tag tag) {
        energy = ((IntTag) tag).intValue();
    }

    public void setChanged() {
    }
}
