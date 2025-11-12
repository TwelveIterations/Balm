package net.blay09.mods.balm.api.event.client.screen;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.screens.Screen;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.KeyPress} and {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.KeyRelease} instead.
 */
@Deprecated
public abstract class ScreenKeyEvent extends BalmEvent {
    private final Screen screen;
    private final int key;
    private final int scanCode;
    private final int modifiers;

    public ScreenKeyEvent(Screen screen, int key, int scanCode, int modifiers) {
        this.screen = screen;
        this.key = key;
        this.scanCode = scanCode;
        this.modifiers = modifiers;
    }

    public Screen getScreen() {
        return screen;
    }

    public int getKey() {
        return key;
    }

    public int getScanCode() {
        return scanCode;
    }

    public int getModifiers() {
        return modifiers;
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.KeyPress} instead.
     */
    @Deprecated
    public static class Press extends ScreenKeyEvent {
        public Press(Screen screen, int key, int scanCode, int modifiers) {
            super(screen, key, scanCode, modifiers);
        }

        /**
         * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.KeyPress#PRE} instead.
         */
        @Deprecated
        public static class Pre extends Press {
            public Pre(Screen screen, int key, int scanCode, int modifiers) {
                super(screen, key, scanCode, modifiers);
            }
        }

        /**
         * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.KeyPress#POST} instead.
         */
        @Deprecated
        public static class Post extends Press {
            public Post(Screen screen, int key, int scanCode, int modifiers) {
                super(screen, key, scanCode, modifiers);
            }
        }
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.KeyRelease} instead.
     */
    @Deprecated
    public static class Release extends ScreenKeyEvent {
        public Release(Screen screen, int key, int scanCode, int modifiers) {
            super(screen, key, scanCode, modifiers);
        }

        /**
         * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.KeyRelease#PRE} instead.
         */
        @Deprecated
        public static class Pre extends Release {
            public Pre(Screen screen, int key, int scanCode, int modifiers) {
                super(screen, key, scanCode, modifiers);
            }
        }

        /**
         * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.KeyRelease#POST} instead.
         */
        @Deprecated
        public static class Post extends Release {
            public Post(Screen screen, int key, int scanCode, int modifiers) {
                super(screen, key, scanCode, modifiers);
            }
        }
    }
}
