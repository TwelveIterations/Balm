package net.blay09.mods.balm.platform.fluid;

import net.minecraft.world.level.material.Fluid;

public interface FluidTank {
    default FluidAmount fill(int slot, Fluid fluid, FluidAmount maxFill, boolean simulate) {
        return new FluidAmount(fill(slot, fluid, maxFill.toPlatform(), simulate), FluidAmount.Unit.platform());
    }

    default FluidAmount drain(int slot, Fluid fluid, FluidAmount maxDrain, boolean simulate) {
        return new FluidAmount(drain(slot, fluid, maxDrain.toPlatform(), simulate), FluidAmount.Unit.platform());
    }

    default void setFluid(int slot, Fluid fluid, FluidAmount amount) {
        setFluid(slot, fluid, amount.toPlatform());
    }

    default FluidAmount getAmount(int slot, FluidAmount.Unit unit) {
        return new FluidAmount(getAmount(slot), FluidAmount.Unit.platform()).to(unit);
    }

    default void setAmount(int slot, FluidAmount amount) {
        setAmount(slot, amount.toPlatform());
    }

    default FluidAmount getCapacity(int slot, FluidAmount.Unit unit) {
        return new FluidAmount(getCapacity(slot), FluidAmount.Unit.platform()).to(unit);
    }

    long fill(int slot, Fluid fluid, long maxFill, boolean simulate);

    long drain(int slot, Fluid fluid, long maxDrain, boolean simulate);

    Fluid getFluid(int slot);

    void setFluid(int slot, Fluid fluid, long amount);

    long getAmount(int slot);

    void setAmount(int slot, long amount);

    long getCapacity(int slot);

    boolean canDrain(int slot, Fluid fluid);

    boolean canFill(int slot, Fluid fluid);

    boolean isEmpty(int slot);

    int getSlotCount();
}
