package net.blay09.mods.balm.fabric.platform.compatibility.milk.internal;

import net.blay09.mods.balm.fabric.internal.FabricBalm;
import net.blay09.mods.balm.platform.compatibility.milk.BalmModSupportMilkFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FabricModSupportMilkFluid implements BalmModSupportMilkFluid {
    private Fluid milkFluid = Fluids.EMPTY;

    @Override
    public void enable() {
        milkFluid = FabricBalm.getProxy().enableMilkFluid();
    }

    @Override
    public Fluid get() {
        return milkFluid;
    }
}
