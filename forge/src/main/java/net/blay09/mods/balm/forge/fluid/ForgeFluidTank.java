package net.blay09.mods.balm.forge.fluid;

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
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return new FluidStack(fluidTank.getFluid(), fluidTank.getAmount());
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluidTank.getCapacity();
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return fluidTank.canFill(stack.getFluid());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return fluidTank.fill(resource.getFluid(), resource.getAmount(), action.simulate());
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        int drained = fluidTank.drain(resource.getFluid(), resource.getAmount(), action.simulate());
        return new FluidStack(fluidTank.getFluid(), drained);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int drained = fluidTank.drain(fluidTank.getFluid(), maxDrain, action.simulate());
        return new FluidStack(fluidTank.getFluid(), drained);
    }
}
