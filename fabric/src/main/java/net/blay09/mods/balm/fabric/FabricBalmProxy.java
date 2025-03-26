package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.fabric.fluid.SimpleMilkFluid;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

public class FabricBalmProxy {
    public Fluid enableMilkFluid() {
        final var milk = Registry.register(BuiltInRegistries.FLUID, ResourceLocation.fromNamespaceAndPath("balm", "milk"), new SimpleMilkFluid());
        BuiltInRegistries.FLUID.addAlias(ResourceLocation.fromNamespaceAndPath("balm-fabric", "milk"), ResourceLocation.fromNamespaceAndPath("balm", "milk"));
        return milk;
    }
}
