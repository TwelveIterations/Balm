package net.blay09.mods.balm.api.fluid;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidTank {
    private final int capacity;
    private final int maxFill;
    private final int maxDrain;
    private Fluid fluid = Fluids.EMPTY;
    private int amount;

    public FluidTank(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public FluidTank(int capacity, int maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public FluidTank(int capacity, int maxFill, int maxDrain) {
        this(maxDrain, capacity, maxFill, 0);
    }

    public FluidTank(int maxDrain, int capacity, int maxFill, int amount) {
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
        this.amount = Math.max(0, Math.min(capacity, amount));
    }

    public int fill(Fluid fluid, int maxFill, boolean simulate) {
        if (!canFill(fluid)) {
            return 0;
        }

        if (this.fluid.isSame(Fluids.EMPTY)) {
            this.fluid = fluid;
        }

        int filled = Math.min(capacity - amount, Math.min(this.maxFill, maxFill));
        if (!simulate) {
            amount += filled;
            setChanged();
        }
        return filled;
    }

    public int drain(Fluid fluid, int maxDrain, boolean simulate) {
        if (!canDrain(fluid)) {
            return 0;
        }

        int drained = Math.min(amount, Math.min(this.maxDrain, maxDrain));
        if (!simulate) {
            amount -= drained;
            setChanged();
        }
        return drained;
    }

    public Fluid getFluid() {
        return amount >= 0 ? fluid : Fluids.EMPTY;
    }

    public void setFluid(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
        setChanged();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean canDrain(Fluid fluid) {
        return (this.fluid.isSame(fluid) || this.fluid.isSame(Fluids.EMPTY)) && maxDrain > 0;
    }

    public boolean canFill(Fluid fluid) {
        return (this.fluid.isSame(fluid) || this.fluid.isSame(Fluids.EMPTY)) && maxFill > 0;
    }

    public boolean isEmpty() {
        return amount <= 0 || fluid.isSame(Fluids.EMPTY);
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Fluid", Balm.getRegistries().getKey(fluid).toString());
        tag.putInt("Amount", amount);
        return tag;
    }

    public void deserialize(CompoundTag tag) {
        fluid = Balm.getRegistries().getFluid(ResourceLocation.tryParse(tag.getString("Fluid")));
        amount = tag.getInt("Amount");
    }

    public void setChanged() {
    }
}
