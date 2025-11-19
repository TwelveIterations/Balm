package net.blay09.mods.balm.platform.config.schema;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.config.LoadedConfig;
import net.blay09.mods.balm.platform.config.MutableLoadedConfig;
import net.minecraft.resources.Identifier;

public interface ConfiguredIdentifier extends ConfiguredProperty<Identifier> {
    default Identifier get(LoadedConfig config) {
        return getRaw(config);
    }

    default Identifier get() {
        return get(Balm.config().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, Identifier value) {
        setRaw(config, value);
    }

    default void set(Identifier value) {
        set(Balm.config().getLocalConfig(parentSchema()), value);
    }
}
