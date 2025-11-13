package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public interface ScreenCallback {
    @FunctionalInterface
    interface Render {
        void handle(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta);

        EventMapper<Render> PRE = EventMapper.createUnbound("ScreenCallback.Render.PRE");
        EventMapper<Render> POST = EventMapper.createUnbound("ScreenCallback.Render.POST");
    }

    @FunctionalInterface
    interface Open {
        Screen handle(Screen screen);

        EventMapper<Open> EVENT = EventMapper.createUnbound("ScreenCallback.Open");
    }

    @FunctionalInterface
    interface Init {
        void handle(Screen screen);

        EventMapper<Init> PRE = EventMapper.createUnbound("ScreenCallback.Init.PRE");
        EventMapper<Init> POST = EventMapper.createUnbound("ScreenCallback.Init.POST");
    }

    @FunctionalInterface
    interface KeyRelease {
        boolean handle(Screen screen, KeyEvent event);

        EventMapper<KeyRelease> PRE = EventMapper.createUnbound("ScreenCallback.KeyRelease.PRE");
        EventMapper<KeyRelease> POST = EventMapper.createUnbound("ScreenCallback.KeyRelease.POST");
    }

    @FunctionalInterface
    interface KeyPress {
        boolean handle(Screen screen, KeyEvent event);

        EventMapper<KeyPress> PRE = EventMapper.createUnbound("ScreenCallback.KeyPress.PRE");
        EventMapper<KeyPress> POST = EventMapper.createUnbound("ScreenCallback.KeyPress.POST");
    }

    @FunctionalInterface
    interface MousePress {
        boolean handle(Screen screen, MouseButtonEvent event, boolean consumed);

        EventMapper<MousePress> PRE = EventMapper.createUnbound("ScreenCallback.MousePress.PRE");
        EventMapper<MousePress> POST = EventMapper.createUnbound("ScreenCallback.MousePress.POST");
    }

    @FunctionalInterface
    interface MouseRelease {
        boolean handle(Screen screen, MouseButtonEvent event, boolean consumed);

        EventMapper<MouseRelease> PRE = EventMapper.createUnbound("ScreenCallback.MouseRelease.PRE");
        EventMapper<MouseRelease> POST = EventMapper.createUnbound("ScreenCallback.MouseRelease.POST");
    }

    @FunctionalInterface
    interface MouseDrag {
        boolean handle(Screen screen, MouseButtonEvent event, double horizontalAmount, double verticalAmount, boolean consumed);

        EventMapper<MouseDrag> PRE = EventMapper.createUnbound("ScreenCallback.MouseDrag.PRE");
        EventMapper<MouseDrag> POST = EventMapper.createUnbound("ScreenCallback.MouseDrag.POST");
    }

    @FunctionalInterface
    interface MouseScroll {
        boolean handle(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount, boolean consumed);

        EventMapper<MouseScroll> PRE = EventMapper.createUnbound("ScreenCallback.MouseScroll.PRE");
        EventMapper<MouseScroll> POST = EventMapper.createUnbound("ScreenCallback.MouseScroll.POST");
    }
}
