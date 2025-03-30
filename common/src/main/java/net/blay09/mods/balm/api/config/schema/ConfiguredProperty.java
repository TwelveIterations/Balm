package net.blay09.mods.balm.api.config.schema;

import com.mojang.serialization.Codec;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.blay09.mods.balm.common.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

public interface ConfiguredProperty<T> {
    BalmConfigSchema parentSchema();

    String category();

    String name();

    String comment();

    boolean synced();

    Class<?> type();

    Codec<T> codec();

    StreamCodec<FriendlyByteBuf, T> streamCodec();

    T defaultValue();

    default T getRaw(LoadedConfig config) {
        return config.getRaw(this);
    }

    default void setRaw(MutableLoadedConfig config, T value) {
        config.setRaw(this, value);
    }
}
