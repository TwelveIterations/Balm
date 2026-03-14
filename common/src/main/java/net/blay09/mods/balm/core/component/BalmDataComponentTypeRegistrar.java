package net.blay09.mods.balm.core.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Provides convenience access to registering data component types.
 */
public interface BalmDataComponentTypeRegistrar {
    void addAlias(Identifier oldId, Identifier newId);

    void addAlias(String oldName, String newName);

    default <T> BalmDataComponentTypeRegistration<T> register(String name, Codec<T> codec) {
        return register(name, (id, builder) -> this.<T>createBuilder().persistent(codec));
    }

    default <T> BalmDataComponentTypeRegistration<T> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return register(name, (id, builder) -> this.<T>createBuilder().persistent(codec).networkSynchronized(streamCodec));
    }

    default <T> BalmDataComponentTypeRegistration<T> register(String name, Function<DataComponentType.Builder<T>, DataComponentType.Builder<T>> builderConsumer) {
        return register(name, (id, builder) -> builderConsumer.apply(builder));
    }

    <T> BalmDataComponentTypeRegistration<T> register(String name, BiFunction<Identifier, DataComponentType.Builder<T>, DataComponentType.Builder<T>> constructor);

    <T> DataComponentType.Builder<T> createBuilder();
}
