package net.blay09.mods.balm.world.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Provides convenience access to registering data component types.
 */
public interface BalmDataComponentTypeFactory {

    default <T> BalmDataComponentTypeRegistration<T> register(String name, Function<DataComponentType.Builder<T>, DataComponentType.Builder<T>> builderConsumer) {
        return register(name, (id, builder) -> builderConsumer.apply(builder));
    }

    <T> BalmDataComponentTypeRegistration<T> register(String name, BiFunction<ResourceLocation, DataComponentType.Builder<T>, DataComponentType.Builder<T>> constructor);

    <T> DataComponentType.Builder<T> createBuilder();
}
