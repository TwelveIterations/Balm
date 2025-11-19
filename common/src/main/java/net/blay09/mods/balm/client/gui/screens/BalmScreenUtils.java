package net.blay09.mods.balm.client.gui.screens;

import net.blay09.mods.balm.internal.mixin.ScreenAccessor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Predicate;

public class BalmScreenUtils {
    public static <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(Screen screen, T widget) {
        return ((ScreenAccessor) screen).balm$addRenderableWidget(widget);
    }

    public static void removeWidgetIf(Screen screen, Predicate<Object> widgetPredicate) {
        ((ScreenAccessor) screen).balm$getChildren().removeIf(widgetPredicate);
        ((ScreenAccessor) screen).balm$getNarratables().removeIf(widgetPredicate);
        ((ScreenAccessor) screen).balm$getRenderables().removeIf(widgetPredicate);
    }
}
