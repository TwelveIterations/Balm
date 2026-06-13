package net.blay09.mods.balm.platform.fluid;

import net.minecraft.world.level.material.Fluid;

public interface FluidTank {
    int fill(int slot, Fluid fluid, int maxFill, boolean simulate);

    int drain(int slot, Fluid fluid, int maxDrain, boolean simulate);

    Fluid getFluid(int slot);

    void setFluid(int slot, Fluid fluid, int amount);

    int getAmount(int slot);

    void setAmount(int slot, int amount);

    int getCapacity(int slot);

    boolean canDrain(int slot, Fluid fluid);

    boolean canFill(int slot, Fluid fluid);

    boolean isEmpty(int slot);

    int getSlotCount();
}
