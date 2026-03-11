package net.blay09.mods.balm.fabric.client.internal.platform.internal;

import net.blay09.mods.balm.fabric.platform.internal.FabricBalmProxy;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;

public class FabricBalmClientProxy extends FabricBalmProxy {

    @Override
    public Fluid enableMilkFluid() {
        final var fluid = super.enableMilkFluid();
        FluidRenderingRegistry.register(fluid, new FluidModel.Unbaked(
                new Material(Identifier.withDefaultNamespace("block/water_still")),
                new Material(Identifier.withDefaultNamespace("block/water_flow")),
                new Material(Identifier.withDefaultNamespace("block/water_overlay")),
                BlockTintSources.constant(0xFFFFFFFF)));
        return fluid;
    }
}
