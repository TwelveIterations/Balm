package net.blay09.mods.balm.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Function;

@Deprecated
public interface BalmRegistries {
    default void enableMilkFluid() {
        Balm.getModSupport().milkFluid().enable();
    }

    default Fluid getMilkFluid() {
        return Balm.getModSupport().milkFluid().get();
    }

    default <T> DeferredObject<T> register(Registry<T> registryId, Function<Identifier, T> constructor, Identifier identifier) {
        final var holder = Balm.getRuntime().registrar().register(ResourceKey.create(registryId.key(), identifier), constructor);
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    BalmRegistries LEGACY = new BalmRegistries() {
    };
}
