package net.blay09.mods.balm.api.fluid;

import net.minecraft.core.Direction;

public interface BalmFluidTankProvider {
    FluidTank getFluidTank();

    default FluidTank getFluidTank(Direction side) {
        return getFluidTank();
    }
}
