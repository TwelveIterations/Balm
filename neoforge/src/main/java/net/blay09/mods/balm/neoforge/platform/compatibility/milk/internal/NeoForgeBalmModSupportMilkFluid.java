package net.blay09.mods.balm.neoforge.platform.compatibility.milk.internal;

import net.blay09.mods.balm.platform.compatibility.milk.BalmModSupportMilkFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForgeMod;

public class NeoForgeBalmModSupportMilkFluid implements BalmModSupportMilkFluid {
    @Override
    public void enable() {
        NeoForgeMod.enableMilkFluid();
    }

    @Override
    public Fluid get() {
        return NeoForgeMod.MILK.get();
    }
}
