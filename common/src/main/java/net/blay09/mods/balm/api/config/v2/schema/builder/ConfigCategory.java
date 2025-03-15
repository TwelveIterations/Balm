package net.blay09.mods.balm.api.config.v2.schema.builder;

import net.blay09.mods.balm.api.config.v2.schema.BalmConfigSchema;
import net.blay09.mods.balm.api.config.v2.schema.ConfiguredProperty;

import java.util.List;

public interface ConfigCategory {
    BalmConfigSchema parentSchema();
    String name();
    String comment();
    List<ConfiguredProperty<?>> properties();
}
