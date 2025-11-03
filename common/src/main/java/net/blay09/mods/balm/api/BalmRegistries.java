package net.blay09.mods.balm.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Function;
import java.util.function.Supplier;

@Deprecated
public interface BalmRegistries {
    default void enableMilkFluid() {
        Balm.getModSupport().milkFluid().enable();
    }

    default Fluid getMilkFluid() {
        return Balm.getModSupport().milkFluid().get();
    }

    default <T> DeferredObject<T> register(Registry<T> registryId, Function<ResourceLocation, T> constructor, ResourceLocation identifier) {
        final var holder = Balm.registrar().register(ResourceKey.create(registryId.key(), identifier), constructor);
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    BalmRegistries LEGACY = new BalmRegistries() {
    };
}
