package net.blay09.mods.balm.api.event.client.screen;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.screens.Screen;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.Init} instead.
 */
@Deprecated
public abstract class ScreenInitEvent extends BalmEvent {
    private final Screen screen;

    public ScreenInitEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.Init#PRE} instead.
     */
    @Deprecated
    public static class Pre extends ScreenInitEvent {
        public Pre(Screen screen) {
            super(screen);
        }
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.Init#POST} instead.
     */
    @Deprecated
    public static class Post extends ScreenInitEvent {
        public Post(Screen screen) {
            super(screen);
        }
    }
}
