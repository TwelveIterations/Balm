package net.blay09.mods.balm.client.platform.event.callback;

import net.blay09.mods.balm.platform.event.EventMapper;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jspecify.annotations.Nullable;

public interface ScreenCallback {
    @FunctionalInterface
    interface Render {
        void render(Screen screen, GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float tickDelta);

        EventMapper<Render> BEFORE = EventMapper.createUnbound("ScreenCallback.Render.Before");
        EventMapper<Render> AFTER_BACKGROUND = EventMapper.createUnbound("ScreenCallback.Render.AfterBackground");
        EventMapper<Render> AFTER = EventMapper.createUnbound("ScreenCallback.Render.After");
    }

    @FunctionalInterface
    interface Opening {
        @Nullable Screen modifyScreen(@Nullable Screen screen);

        EventMapper<Opening> EVENT = EventMapper.createUnbound("ScreenCallback.Opening");
    }

    interface Init {
        @FunctionalInterface
        interface Before {
            void beforeInit(Screen screen);

            EventMapper<Before> EVENT = EventMapper.createUnbound("ScreenCallback.Init.Before");
        }

        @FunctionalInterface
        interface After {
            void afterInit(Screen screen);

            EventMapper<After> EVENT = EventMapper.createUnbound("ScreenCallback.Init.AFter");
        }
    }

    interface KeyRelease {
        @FunctionalInterface
        interface Before {
            boolean keyReleased(Screen screen, KeyEvent event);

            EventMapper<Before> EVENT = EventMapper.createUnbound("ScreenCallback.KeyRelease.Before");
        }

        @FunctionalInterface
        interface After {
            void afterKeyReleased(Screen screen, KeyEvent event);

            EventMapper<After> EVENT = EventMapper.createUnbound("ScreenCallback.KeyRelease.After");
        }
    }

    interface KeyPress {
        @FunctionalInterface
        interface Before {
            boolean keyPressed(Screen screen, KeyEvent event);

            EventMapper<Before> EVENT = EventMapper.createUnbound("ScreenCallback.KeyPress.Before");
        }

        @FunctionalInterface
        interface After {
            void afterKeyPressed(Screen screen, KeyEvent event);

            EventMapper<After> EVENT = EventMapper.createUnbound("ScreenCallback.KeyPress.After");
        }
    }

    interface MousePress {
        @FunctionalInterface
        interface Before {
            boolean mousePressed(Screen screen, MouseButtonEvent event);

            EventMapper<Before> EVENT = EventMapper.createUnbound("ScreenCallback.MousePress.Before");
        }

        @FunctionalInterface
        interface After {
            boolean afterMousePressed(Screen screen, MouseButtonEvent event, boolean consumed);

            EventMapper<After> EVENT = EventMapper.createUnbound("ScreenCallback.MousePress.After");
        }
    }

    interface MouseRelease {
        @FunctionalInterface
        interface Before {
            boolean mouseReleased(Screen screen, double mouseX, double mouseY, int button);

            EventMapper<Before> EVENT = EventMapper.createUnbound("ScreenCallback.MouseRelease.Before");
        }

        @FunctionalInterface
        interface After {
            boolean afterMouseReleased(Screen screen, double mouseX, double mouseY, int button, boolean consumed);

            EventMapper<After> EVENT = EventMapper.createUnbound("ScreenCallback.MouseRelease.After");
        }
    }

    interface MouseDrag {
        @FunctionalInterface
        interface Before {
            boolean mouseDragged(Screen screen, double mouseX, double mouseY, int button, double horizontalAmount, double verticalAmount);

            EventMapper<Before> EVENT = EventMapper.createUnbound("ScreenCallback.MouseDrag.Before");
        }

        @FunctionalInterface
        interface After {
            boolean afterMouseDragged(Screen screen, double mouseX, double mouseY, int button, double horizontalAmount, double verticalAmount, boolean consumed);

            EventMapper<After> EVENT = EventMapper.createUnbound("ScreenCallback.MouseDrag.After");
        }
    }

    interface MouseScroll {
        @FunctionalInterface
        interface Before {
            boolean mouseScrolled(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount);

            EventMapper<Before> EVENT = EventMapper.createUnbound("ScreenCallback.MouseScroll.Before");
        }

        @FunctionalInterface
        interface After {
            boolean afterMouseScrolled(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount, boolean consumed);

            EventMapper<After> EVENT = EventMapper.createUnbound("ScreenCallback.MouseScroll.After");
        }
    }
}
