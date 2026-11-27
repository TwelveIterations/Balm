package net.blay09.mods.balm.platform.transfer;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public record TransactionalFluid(Fluid fluid, DataComponentPatch components) implements TransactionalResource {
    public TransactionalFluid {
        if (fluid instanceof FlowingFluid flowing) {
            fluid = flowing.getSource().getType();
        }
        if (fluid == Fluids.EMPTY) {
            components = DataComponentPatch.EMPTY;
        }
    }

    public static TransactionalFluid of(Fluid fluid) {
        return new TransactionalFluid(fluid, DataComponentPatch.EMPTY);
    }

    @Override
    public boolean isEmpty() {
        return fluid == Fluids.EMPTY;
    }
}
