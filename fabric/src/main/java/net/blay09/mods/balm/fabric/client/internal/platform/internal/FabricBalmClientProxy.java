package net.blay09.mods.balm.fabric.client.internal.platform.internal;

import net.blay09.mods.balm.fabric.platform.internal.FabricBalmProxy;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.world.level.material.Fluid;

public class FabricBalmClientProxy extends FabricBalmProxy {

    @Override
    public Fluid enableMilkFluid() {
        final var fluid = super.enableMilkFluid();
        FluidRenderHandlerRegistry.INSTANCE.register(fluid, SimpleFluidRenderHandler.coloredWater(0xFFFFFFFF));
        return fluid;
    }
}
