package net.blay09.mods.balm.api.event.client.screen;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.screens.Screen;

/**
 * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MousePress}, {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MousePress}, {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MouseRelease} and {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MouseDrag} instead.
 */
@Deprecated
public abstract class ScreenMouseEvent extends BalmEvent {
    private final Screen screen;
    private final double mouseX;
    private final double mouseY;
    private final int button;

    public ScreenMouseEvent(Screen screen, double mouseX, double mouseY, int button) {
        this.screen = screen;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
    }

    public Screen getScreen() {
        return screen;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public int getButton() {
        return button;
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MousePress} instead.
     */
    @Deprecated
    public static abstract class Click extends ScreenMouseEvent {
        public Click(Screen screen, double mouseX, double mouseY, int button) {
            super(screen, mouseX, mouseY, button);
        }

        /**
         * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MousePress#BEFORE} instead.
         */
        @Deprecated
        public static class Pre extends Click {
            public Pre(Screen screen, double mouseX, double mouseY, int button) {
                super(screen, mouseX, mouseY, button);
            }
        }

        /**
         * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MousePress#AFTER} instead.
         */
        @Deprecated
        public static class Post extends Click {
            public Post(Screen screen, double mouseX, double mouseY, int button) {
                super(screen, mouseX, mouseY, button);
            }
        }
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MouseRelease} instead.
     */
    @Deprecated
    public static abstract class Release extends ScreenMouseEvent {
        public Release(Screen screen, double mouseX, double mouseY, int button) {
            super(screen, mouseX, mouseY, button);
        }

        /**
         * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MouseRelease#BEFORE} instead.
         */
        @Deprecated
        public static class Pre extends Release {
            public Pre(Screen screen, double mouseX, double mouseY, int button) {
                super(screen, mouseX, mouseY, button);
            }
        }

        /**
         * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MouseRelease#AFTER} instead.
         */
        @Deprecated
        public static class Post extends Release {
            public Post(Screen screen, double mouseX, double mouseY, int button) {
                super(screen, mouseX, mouseY, button);
            }
        }
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MouseDrag} instead.
     */
    @Deprecated
    public static abstract class Drag extends ScreenMouseEvent {
        private final double dragX;
        private final double dragY;

        public Drag(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
            super(screen, mouseX, mouseY, button);
            this.dragX = dragX;
            this.dragY = dragY;
        }

        public double getDragX() {
            return dragX;
        }

        public double getDragY() {
            return dragY;
        }

        /**
         * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MouseDrag#BEFORE} instead.
         */
        @Deprecated
        public static class Pre extends Drag {
            public Pre(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
                super(screen, mouseX, mouseY, button, dragX, dragY);
            }
        }

        /**
         * @deprecated Use {@link net.blay09.mods.balm.client.event.callback.ScreenCallback.MouseDrag#AFTER} instead.
         */
        @Deprecated
        public static class Post extends Drag {
            public Post(Screen screen, double mouseX, double mouseY, int button, double dragX, double dragY) {
                super(screen, mouseX, mouseY, button, dragX, dragY);
            }
        }
    }
}
