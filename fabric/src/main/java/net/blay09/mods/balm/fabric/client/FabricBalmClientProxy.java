package net.blay09.mods.balm.fabric.client;

import net.blay09.mods.balm.fabric.FabricBalmProxy;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.world.level.material.Fluid;

public class FabricBalmClientProxy extends FabricBalmProxy {

    @Override
    public Fluid enableMilkFluid() {
        final var fluid = super.enableMilkFluid();
        FluidRenderHandlerRegistry.INSTANCE.register(fluid,
                new SimpleFluidRenderHandler(SimpleFluidRenderHandler.WATER_STILL, SimpleFluidRenderHandler.WATER_FLOWING, 0xFFFFFFFF));
        return fluid;
    }
}
