package net.blay09.mods.balm.platform.config.schema;

import com.mojang.serialization.DataResult;

public interface ConfigValidator<T> {
    DataResult<T> validate(T value);
}
