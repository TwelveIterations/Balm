package net.blay09.mods.balm.neoforge;

import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.function.Function;

public class NeoForgeBalmRegistries implements BalmRegistries {
    @Override
    public void enableMilkFluid() {
        NeoForgeMod.enableMilkFluid();
    }

    @Override
    public Fluid getMilkFluid() {
        return NeoForgeMod.MILK.get();
    }

    @Override
    public <T> DeferredObject<T> register(Registry<T> registry, Function<ResourceLocation, T> supplier, ResourceLocation identifier) {
        final var register = DeferredRegisters.get(registry.key(), identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), () -> supplier.apply(identifier));
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }
}
