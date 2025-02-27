package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmRegistries;
import net.minecraft.world.level.material.Fluid;

public class FabricBalmRegistries implements BalmRegistries {
    public Fluid milkFluid;

    @Override
    public void enableMilkFluid() {
        milkFluid = FabricBalm.getProxy().enableMilkFluid();
    }

    @Override
    public Fluid getMilkFluid() {
        return milkFluid;
    }
}
