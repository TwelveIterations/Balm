package net.blay09.mods.balm.forge.compat.milk;

import net.blay09.mods.balm.api.compat.milk.BalmModSupportMilkFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;

public class ForgeBalmModSupportMilkFluid implements BalmModSupportMilkFluid {
    @Override
    public void enable() {
        ForgeMod.enableMilkFluid();
    }

    @Override
    public Fluid get() {
        return ForgeMod.MILK.get();
    }
}
