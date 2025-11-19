package net.blay09.mods.balm.platform.fluid;

import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

public interface BalmFluidTankProvider {
    @Nullable
    FluidTank getFluidTank();

    @Nullable
    default FluidTank getFluidTank(Direction side) {
        return getFluidTank();
    }
}
