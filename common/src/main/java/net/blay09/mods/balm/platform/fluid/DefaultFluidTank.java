package net.blay09.mods.balm.platform.fluid;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class DefaultFluidTank implements FluidTank {
    private final int capacity;
    private final int maxFill;
    private final int maxDrain;
    private Fluid fluid = Fluids.EMPTY;
    private int amount;

    public DefaultFluidTank(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public DefaultFluidTank(int capacity, int maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public DefaultFluidTank(int capacity, int maxFill, int maxDrain) {
        this(maxDrain, capacity, maxFill, 0);
    }

    public DefaultFluidTank(int maxDrain, int capacity, int maxFill, int amount) {
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
        this.amount = Math.max(0, Math.min(capacity, amount));
    }

    @Override
    public int fill(int slot, Fluid fluid, int maxFill, boolean simulate) {
        if (!canFill(slot, fluid)) {
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

    @Override
    public int drain(int slot, Fluid fluid, int maxDrain, boolean simulate) {
        if (!canDrain(slot, fluid)) {
            return 0;
        }

        int drained = Math.min(amount, Math.min(this.maxDrain, maxDrain));
        if (!simulate) {
            amount -= drained;
            setChanged();
        }
        return drained;
    }

    @Override
    public Fluid getFluid(int slot) {
        return amount >= 0 ? fluid : Fluids.EMPTY;
    }

    @Override
    public void setFluid(int slot, Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = amount;
        setChanged();
    }

    @Override
    public int getAmount(int slot) {
        return amount;
    }

    @Override
    public void setAmount(int slot, int amount) {
        this.amount = amount;
    }

    @Override
    public int getCapacity(int slot) {
        return capacity;
    }

    @Override
    public boolean canDrain(int slot, Fluid fluid) {
        return (this.fluid.isSame(fluid) || this.fluid.isSame(Fluids.EMPTY)) && maxDrain > 0;
    }

    @Override
    public boolean canFill(int slot, Fluid fluid) {
        return (this.fluid.isSame(fluid) || this.fluid.isSame(Fluids.EMPTY)) && maxFill > 0;
    }

    @Override
    public boolean isEmpty(int slot) {
        return amount <= 0 || fluid.isSame(Fluids.EMPTY);
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    public void serialize(ValueOutput output) {
        output.putString("Fluid", BuiltInRegistries.FLUID.getKey(fluid).toString());
        output.putInt("Amount", amount);
    }

    public void deserialize(ValueInput input) {
        fluid = input.getString("Fluid").map(Identifier::tryParse).map(BuiltInRegistries.FLUID::getValue).orElse(Fluids.EMPTY);
        amount = input.getIntOr("Amount", 0);
    }

    public void setChanged() {
    }
}
