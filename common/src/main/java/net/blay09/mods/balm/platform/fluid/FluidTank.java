package net.blay09.mods.balm.platform.fluid;

import net.minecraft.world.level.material.Fluid;

public interface FluidTank {
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
