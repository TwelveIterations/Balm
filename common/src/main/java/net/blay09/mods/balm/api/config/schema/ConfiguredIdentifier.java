package net.blay09.mods.balm.api.config.schema;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.LoadedConfig;
import net.blay09.mods.balm.api.config.MutableLoadedConfig;
import net.minecraft.resources.Identifier;

public interface ConfiguredIdentifier extends ConfiguredProperty<Identifier> {
    default Identifier get(LoadedConfig config) {
        return getRaw(config);
    }

    default Identifier get() {
        return get(Balm.getConfig().getActiveConfig(parentSchema()));
    }

    default void set(MutableLoadedConfig config, Identifier value) {
        setRaw(config, value);
    }

    default void set(Identifier value) {
        set(Balm.getConfig().getLocalConfig(parentSchema()), value);
    }
}
