package net.blay09.mods.balm.api.config.v2;

import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;

public class DefaultedConfig implements LoadedConfig {

    public static final DefaultedConfig INSTANCE = new DefaultedConfig();

    @Override
    public <T> T getRaw(ConfiguredProperty<T> property) {
        return property.defaultValue();
    }
}
