package net.blay09.mods.balm.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Function;

public interface BalmRegistries {
    void enableMilkFluid();

    Fluid getMilkFluid();

    <T> DeferredObject<T> register(Registry<T> registryId, Function<ResourceLocation, T> supplier, ResourceLocation identifier);
}
