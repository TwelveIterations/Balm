package net.blay09.mods.balm.platform.config.schema;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public interface ConfiguredProperty<T> {
    BalmConfigSchema parentSchema();

    String category();

    String name();

    String comment();

    boolean synced();

    Optional<Identifier> customControl();

    Class<?> type();

    Codec<T> codec();

    StreamCodec<ByteBuf, T> streamCodec();

    T defaultValue();

    default DataResult<T> validateValue(T value) {
        return DataResult.success(value);
    }

    default T getRaw(LoadedConfig config) {
        return config.getRaw(this);
    }

    default void setRaw(MutableLoadedConfig config, T value) {
        config.setRaw(this, value);
    }
}
