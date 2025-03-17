package net.blay09.mods.balm.api.config.schema.builder;

import net.blay09.mods.balm.api.config.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.schema.ConfiguredProperty;

import java.util.List;

public interface ConfigCategory {
    BalmConfigSchema parentSchema();
    String name();
    String comment();
    List<ConfiguredProperty<?>> properties();
}
