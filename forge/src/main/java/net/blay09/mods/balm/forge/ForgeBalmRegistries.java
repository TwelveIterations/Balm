package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;

public class ForgeBalmRegistries implements BalmRegistries {
    @Override
    public void enableMilkFluid() {
        ForgeMod.enableMilkFluid();
    }

    @Override
    public Fluid getMilkFluid() {
        return ForgeMod.MILK.get();
    }
}
