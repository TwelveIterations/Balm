package net.blay09.mods.balm.fabric.platform.internal;

import net.blay09.mods.balm.fabric.platform.compatibility.milk.internal.SimpleMilkFluid;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;

public class FabricBalmProxy {
    public Fluid enableMilkFluid() {
        final var milk = Registry.register(BuiltInRegistries.FLUID, Identifier.fromNamespaceAndPath("balm", "milk"), new SimpleMilkFluid());
        BuiltInRegistries.FLUID.addAlias(Identifier.fromNamespaceAndPath("balm-fabric", "milk"), Identifier.fromNamespaceAndPath("balm", "milk"));
        return milk;
    }
}
