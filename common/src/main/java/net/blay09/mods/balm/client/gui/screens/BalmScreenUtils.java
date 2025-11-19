package net.blay09.mods.balm.client.gui.screens;

import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

public class BalmScreenUtils {
    public static <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(Screen screen, T widget) {
        return ((ScreenAccessor) screen).balm$addRenderableWidget(widget);
    }
}
