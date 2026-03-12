package net.blay09.mods.balm.client.platform.config;

import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface BalmConfigScreenFactory {
    Screen create(Screen parent);
}
