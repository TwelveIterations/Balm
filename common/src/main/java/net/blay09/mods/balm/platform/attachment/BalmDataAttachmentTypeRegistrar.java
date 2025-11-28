package net.blay09.mods.balm.platform.attachment;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmDataAttachmentTypeRegistrar {

    default <T> BalmDataAttachmentTypeRegistration<T> register(String name, Codec<T> codec) {
        return register(name, (id, builder) -> builder.persistent(codec));
    }

    default <T> BalmDataAttachmentTypeRegistration<T> register(String name, Codec<T> codec, Supplier<T> initializer) {
        return register(name, (id, builder) -> builder.persistent(codec).initializer(initializer));
    }

    default <T> BalmDataAttachmentTypeRegistration<T> register(String name, Codec<T> codec, Supplier<T> initializer, boolean copyOnDeath) {
        return register(name, (id, builder) -> {
            builder.persistent(codec).initializer(initializer);
            if (copyOnDeath) {
                builder.copyOnDeath();
            }
            return builder;
        });
    }

    default <T> BalmDataAttachmentTypeRegistration<T> register(String name, Function<DataAttachmentTypeBuilder<T>, DataAttachmentTypeBuilder<T>> builderConsumer) {
        return register(name, (id, builder) -> builderConsumer.apply(builder));
    }

    <T> BalmDataAttachmentTypeRegistration<T> register(String name, BiFunction<ResourceLocation, DataAttachmentTypeBuilder<T>, DataAttachmentTypeBuilder<T>> constructor);

}
