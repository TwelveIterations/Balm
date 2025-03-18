package net.blay09.mods.balm.api.fluid;

import net.minecraft.world.level.material.Fluid;

public interface FluidTank {
    int fill(Fluid fluid, int maxFill, boolean simulate);

    int drain(Fluid fluid, int maxDrain, boolean simulate);

    Fluid getFluid();

    void setFluid(Fluid fluid, int amount);

    int getAmount();

    void setAmount(int amount);

    int getCapacity();

    boolean canDrain(Fluid fluid);

    boolean canFill(Fluid fluid);

    boolean isEmpty();
}
