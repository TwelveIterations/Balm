package net.blay09.mods.balm.forge.platform.fluid.internal;

import com.google.common.primitives.Ints;
import net.blay09.mods.balm.platform.fluid.FluidTank;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ForgeFluidTank implements IFluidHandler {

    private final FluidTank fluidTank;

    public ForgeFluidTank(FluidTank fluidTank) {
        this.fluidTank = fluidTank;
    }

    @Override
    public int getTanks() {
        return fluidTank.getSlotCount();
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return new FluidStack(fluidTank.getFluid(tank), Ints.saturatedCast(fluidTank.getAmount(tank)));
    }

    @Override
    public int getTankCapacity(int tank) {
        return Ints.saturatedCast(fluidTank.getCapacity(tank));
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return fluidTank.canFill(tank, stack.getFluid());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int filled = 0;
        for (int i = 0; i < fluidTank.getSlotCount(); i++) {
            if (filled >= resource.getAmount()) {
                break;
            }

            if (fluidTank.canFill(i, resource.getFluid())) {
                filled += Math.toIntExact(fluidTank.fill(i, resource.getFluid(), resource.getAmount() - filled, action.simulate()));
            }
        }

        return filled;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        int drained = 0;
        for (int i = 0; i < fluidTank.getSlotCount(); i++) {
            if (drained >= resource.getAmount()) {
                break;
            }

            if (fluidTank.canDrain(i, resource.getFluid())) {
                drained += Math.toIntExact(fluidTank.drain(i, resource.getFluid(), resource.getAmount() - drained, action.simulate()));
            }
        }

        return drained > 0 ? new FluidStack(resource.getFluid(), drained) : FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        for (int i = 0; i < fluidTank.getSlotCount(); i++) {
            final var fluid = fluidTank.getFluid(i);
            int drained = Math.toIntExact(fluidTank.drain(i, fluid, maxDrain, action.simulate()));
            if (drained > 0) {
                return new FluidStack(fluid, drained);
            }
        }

        return FluidStack.EMPTY;
    }
}
