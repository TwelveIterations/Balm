package net.blay09.mods.balm.client.platform.config.screen;

import net.blay09.mods.balm.client.platform.config.ConfigControlContext;
import net.minecraft.client.gui.components.AbstractWidget;

@FunctionalInterface
public interface BalmConfigScreenWidgetFactory {
    AbstractWidget create(BalmConfigScreen screen, ConfigControlContext context, BalmConfigScreenRowState rowState);
}
