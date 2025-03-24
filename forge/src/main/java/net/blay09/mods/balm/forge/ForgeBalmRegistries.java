package net.blay09.mods.balm.forge;

import net.blay09.mods.balm.api.BalmRegistries;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;

import java.util.function.Function;

public class ForgeBalmRegistries implements BalmRegistries {
    @Override
    public void enableMilkFluid() {
        ForgeMod.enableMilkFluid();
    }

    @Override
    public Fluid getMilkFluid() {
        return ForgeMod.MILK.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DeferredObject<T> register(Registry<T> registry, Function<ResourceLocation, T> supplier, ResourceLocation identifier) {
        final var register = DeferredRegisters.get((ResourceKey<Registry<T>>) registry.key(), identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), () -> supplier.apply(identifier));
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }
}
