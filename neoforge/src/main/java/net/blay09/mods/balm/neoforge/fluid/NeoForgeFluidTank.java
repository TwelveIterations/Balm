package net.blay09.mods.balm.neoforge.fluid;

import net.blay09.mods.balm.api.fluid.FluidTank;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public record NeoForgeFluidTank(FluidTank fluidTank) implements ResourceHandler<FluidResource> {

    @Override
    public int size() {
        return 1;
    }

    @Override
    public FluidResource getResource(int i) {
        return i == 0 ? FluidResource.of(fluidTank.getFluid()) : FluidResource.EMPTY;
    }

    @Override
    public long getAmountAsLong(int i) {
        return i == 0 ? fluidTank.getAmount() : 0;
    }

    @Override
    public long getCapacityAsLong(int i, FluidResource fluidResource) {
        return i == 0 ? fluidTank.getCapacity() : 0;
    }

    @Override
    public boolean isValid(int i, FluidResource fluidResource) {
        return i == 0 && (fluidTank.isEmpty() || fluidResource.is(fluidTank.getFluid()));
    }

    @Override
    public int insert(int i, FluidResource fluidResource, int amount, TransactionContext transactionContext) {
        return i == 0 ? fluidTank.fill(fluidResource.getFluid(), amount, false) : amount;
    }

    @Override
    public int extract(int i, FluidResource fluidResource, int amount, TransactionContext transactionContext) {
        return i == 0 ? fluidTank.drain(fluidResource.getFluid(), amount, false) : amount;
    }


}
