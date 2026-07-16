package net.blay09.mods.balm.client.platform.config;

import net.blay09.mods.balm.platform.config.schema.ConfiguredProperty;
import net.minecraft.network.chat.Component;

public interface ConfigControlContext {

    int entryWidth();

    int entryHeight();

    Component label(ConfiguredProperty<?> property);

    Component tooltip(ConfiguredProperty<?> property);

}