package net.blay09.mods.balm.client.event.callback;

import net.blay09.mods.balm.event.EventMapper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

import java.awt.event.MouseWheelEvent;

public interface ScreenCallback {
    @FunctionalInterface
    interface Render {
        void handle(Screen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta);

        EventMapper<Render> PRE = EventMapper.createUnbound();
        EventMapper<Render> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Open {
        Screen handle(Screen screen);

        EventMapper<Open> EVENT = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface Init {
        void handle(Screen screen);

        EventMapper<Init> PRE = EventMapper.createUnbound();
        EventMapper<Init> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface KeyRelease {
        boolean handle(Screen screen, KeyEvent event);

        EventMapper<KeyRelease> PRE = EventMapper.createUnbound();
        EventMapper<KeyRelease> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface KeyPress {
        boolean handle(Screen screen, KeyEvent event);

        EventMapper<KeyPress> PRE = EventMapper.createUnbound();
        EventMapper<KeyPress> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface MousePress {
        boolean handle(Screen screen, MouseButtonEvent event);

        EventMapper<MousePress> PRE = EventMapper.createUnbound();
        EventMapper<MousePress> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface MouseRelease {
        boolean handle(Screen screen, MouseButtonEvent event);

        EventMapper<MouseRelease> PRE = EventMapper.createUnbound();
        EventMapper<MouseRelease> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface MouseDrag {
        boolean handle(Screen screen, MouseButtonEvent event);

        EventMapper<MouseDrag> PRE = EventMapper.createUnbound();
        EventMapper<MouseDrag> POST = EventMapper.createUnbound();
    }

    @FunctionalInterface
    interface MouseScroll {
        boolean handle(Screen screen, MouseWheelEvent event);

        EventMapper<MouseScroll> PRE = EventMapper.createUnbound();
        EventMapper<MouseScroll> POST = EventMapper.createUnbound();
    }
}
