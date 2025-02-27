package net.blay09.mods.balm.fabric;

import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Function;

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

    @Override
    public <T> DeferredObject<T> register(Registry<T> registry, Function<ResourceLocation, T> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier,
                () -> Registry.register(registry, identifier, supplier.apply(identifier))).resolveImmediately();
    }
}
