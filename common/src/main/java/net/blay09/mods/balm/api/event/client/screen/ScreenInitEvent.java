package net.blay09.mods.balm.api.event.client.screen;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.screens.Screen;

public abstract class ScreenInitEvent extends BalmEvent {
    private final Screen screen;

    public ScreenInitEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public static class Pre extends ScreenInitEvent {
        public Pre(Screen screen) {
            super(screen);
        }
    }

    public static class Post extends ScreenInitEvent {
        public Post(Screen screen) {
            super(screen);
        }
    }
}
