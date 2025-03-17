package net.blay09.mods.balm.api.config.schema.builder;

import net.blay09.mods.balm.api.config.schema.impl.ConfigCategoryImpl;

import java.util.function.Function;

public interface ConfigCategoryBuilder extends PropertyHolderBuilder {
    ConfigCategoryImpl comment(String comment);
    <T> T via(Function<ConfigCategoryBuilder, T> initializer);
}
