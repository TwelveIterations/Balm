package net.blay09.mods.balm.platform.fluid;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class DefaultFluidTank implements FluidTank {
    private final long capacity;
    private final long maxFill;
    private final long maxDrain;
    private Fluid fluid = Fluids.EMPTY;
    private long amount;

    public DefaultFluidTank(FluidAmount capacity) {
        this(capacity.toPlatform());
    }

    public DefaultFluidTank(FluidAmount capacity, FluidAmount maxTransfer) {
        this(capacity.toPlatform(), maxTransfer.toPlatform());
    }

    public DefaultFluidTank(FluidAmount capacity, FluidAmount maxFill, FluidAmount maxDrain) {
        this(capacity.toPlatform(), maxFill.toPlatform(), maxDrain.toPlatform());
    }

    public DefaultFluidTank(FluidAmount maxDrain, FluidAmount capacity, FluidAmount maxFill, FluidAmount amount) {
        this(maxDrain.toPlatform(), capacity.toPlatform(), maxFill.toPlatform(), amount.toPlatform());
    }

    public DefaultFluidTank(long capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public DefaultFluidTank(long capacity, long maxTransfer) {
        this(maxTransfer, capacity, maxTransfer, 0);
    }

    public DefaultFluidTank(long capacity, long maxFill, long maxDrain) {
        this(maxDrain, capacity, maxFill, 0);
    }

    public DefaultFluidTank(long maxDrain, long capacity, long maxFill, long amount) {
        this.capacity = capacity;
        this.maxFill = maxFill;
        this.maxDrain = maxDrain;
        this.amount = Math.max(0, Math.min(capacity, amount));
    }

    @Override
    public long fill(int slot, Fluid fluid, long maxFill, boolean simulate) {
        if (slot != 0) {
            return 0;
        }

        if (!canFill(slot, fluid)) {
            return 0;
        }

        if (this.fluid == Fluids.EMPTY) {
            this.fluid = fluid;
        }

        long filled = Math.min(capacity - amount, Math.min(this.maxFill, maxFill));
        if (!simulate) {
            amount += filled;
            setChanged();
        }
        return filled;
    }

    @Override
    public long drain(int slot, Fluid fluid, long maxDrain, boolean simulate) {
        if (slot != 0) {
            return 0;
        }

        if (!canDrain(slot, fluid)) {
            return 0;
        }

        long drained = Math.min(amount, Math.min(this.maxDrain, maxDrain));
        if (!simulate) {
            amount -= drained;
            setChanged();
        }
        return drained;
    }

    @Override
    public Fluid getFluid(int slot) {
        if (slot != 0) {
            return Fluids.EMPTY;
        }

        return amount >= 0 ? fluid : Fluids.EMPTY;
    }

    @Override
    public void setFluid(int slot, Fluid fluid, long amount) {
        if (slot != 0) {
            return;
        }

        this.fluid = fluid;
        this.amount = amount;
        setChanged();
    }

    @Override
    public long getAmount(int slot) {
        if (slot != 0) {
            return 0;
        }

        return amount;
    }

    @Override
    public void setAmount(int slot, long amount) {
        if (slot != 0) {
            return;
        }

        this.amount = amount;
    }

    @Override
    public long getCapacity(int slot) {
        if (slot != 0) {
            return 0;
        }

        return capacity;
    }

    @Override
    public boolean canDrain(int slot, Fluid fluid) {
        if (slot != 0) {
            return false;
        }

        return (this.fluid == fluid || this.fluid == Fluids.EMPTY) && maxDrain > 0;
    }

    @Override
    public boolean canFill(int slot, Fluid fluid) {
        if (slot != 0) {
            return false;
        }

        return (this.fluid == fluid || this.fluid == Fluids.EMPTY) && maxFill > 0;
    }

    @Override
    public boolean isEmpty(int slot) {
        if (slot != 0) {
            return true;
        }

        return amount <= 0 || fluid == Fluids.EMPTY;
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    public void serialize(ValueOutput output) {
        output.putString("Fluid", BuiltInRegistries.FLUID.getKey(fluid).toString());
        output.putLong("Amount", amount);
        output.store("Unit", FluidAmount.Unit.CODEC, FluidAmount.Unit.platform());
    }

    public void deserialize(ValueInput input) {
        fluid = input.getString("Fluid").map(Identifier::tryParse).map(BuiltInRegistries.FLUID::getValue).orElse(Fluids.EMPTY);
        final var unit = input.read("Unit", FluidAmount.Unit.CODEC).orElseGet(FluidAmount.Unit::platform);
        amount = unit.convert(input.getLongOr("Amount", 0L), FluidAmount.Unit.platform());
    }

    public void setChanged() {
    }
}
