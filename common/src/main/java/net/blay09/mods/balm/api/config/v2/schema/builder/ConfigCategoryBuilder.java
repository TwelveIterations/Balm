package net.blay09.mods.balm.api.config.v2.schema.builder;

import net.blay09.mods.balm.api.config.v2.schema.impl.ConfigCategoryImpl;

import java.util.function.Function;

public interface ConfigCategoryBuilder extends PropertyHolderBuilder {
    ConfigCategoryImpl comment(String comment);
    <T> T via(Function<ConfigCategoryBuilder, T> initializer);
}
