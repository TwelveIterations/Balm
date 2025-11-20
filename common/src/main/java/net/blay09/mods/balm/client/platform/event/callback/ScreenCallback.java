package net.blay09.mods.balm.client.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public interface ScreenCallback {
    @FunctionalInterface
    interface Render {
        void handle(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta);

        EventMapper<Render> BEFORE = EventMapper.createUnbound("ScreenCallback.Render.Before");
        EventMapper<Render> AFTER_BACKGROUND = EventMapper.createUnbound("ScreenCallback.Render.AfterBackground");
        EventMapper<Render> AFTER = EventMapper.createUnbound("ScreenCallback.Render.After");
    }

    @FunctionalInterface
    interface Open {
        Screen handle(Screen screen);

        EventMapper<Open> EVENT = EventMapper.createUnbound("ScreenCallback.Open");
    }

    @FunctionalInterface
    interface Init {
        void handle(Screen screen);

        EventMapper<Init> BEFORE = EventMapper.createUnbound("ScreenCallback.Init.Before");
        EventMapper<Init> AFTER = EventMapper.createUnbound("ScreenCallback.Init.After");
    }

    @FunctionalInterface
    interface KeyRelease {
        boolean handle(Screen screen, KeyEvent event);

        EventMapper<KeyRelease> BEFORE = EventMapper.createUnbound("ScreenCallback.KeyRelease.Before");
        EventMapper<KeyRelease> AFTER = EventMapper.createUnbound("ScreenCallback.KeyRelease.After");
    }

    @FunctionalInterface
    interface KeyPress {
        boolean handle(Screen screen, KeyEvent event);

        EventMapper<KeyPress> BEFORE = EventMapper.createUnbound("ScreenCallback.KeyPress.Before");
        EventMapper<KeyPress> AFTER = EventMapper.createUnbound("ScreenCallback.KeyPress.After");
    }

    @FunctionalInterface
    interface MousePress {
        boolean handle(Screen screen, MouseButtonEvent event, boolean consumed);

        EventMapper<MousePress> BEFORE = EventMapper.createUnbound("ScreenCallback.MousePress.Before");
        EventMapper<MousePress> AFTER = EventMapper.createUnbound("ScreenCallback.MousePress.After");
    }

    @FunctionalInterface
    interface MouseRelease {
        boolean handle(Screen screen, double mouseX, double mouseY, int button, boolean consumed);

        EventMapper<MouseRelease> BEFORE = EventMapper.createUnbound("ScreenCallback.MouseRelease.Before");
        EventMapper<MouseRelease> AFTER = EventMapper.createUnbound("ScreenCallback.MouseRelease.After");
    }

    @FunctionalInterface
    interface MouseDrag {
        boolean handle(Screen screen, double mouseX, double mouseY, int button, double horizontalAmount, double verticalAmount, boolean consumed);

        EventMapper<MouseDrag> BEFORE = EventMapper.createUnbound("ScreenCallback.MouseDrag.Before");
        EventMapper<MouseDrag> AFTER = EventMapper.createUnbound("ScreenCallback.MouseDrag.After");
    }

    @FunctionalInterface
    interface MouseScroll {
        boolean handle(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount, boolean consumed);

        EventMapper<MouseScroll> BEFORE = EventMapper.createUnbound("ScreenCallback.MouseScroll.Before");
        EventMapper<MouseScroll> AFTER = EventMapper.createUnbound("ScreenCallback.MouseScroll.After");
    }
}
