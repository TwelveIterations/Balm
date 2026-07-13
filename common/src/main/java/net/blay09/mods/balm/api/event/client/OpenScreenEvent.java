package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class OpenScreenEvent extends BalmEvent {
    private final Screen screen;
    private Screen newScreen;

    public OpenScreenEvent(Screen screen) {
        this.screen = screen;
        this.newScreen = screen;
    }

    public OpenScreenEvent(Screen screen, @Nullable Screen newScreen) {
        this.screen = screen;
        this.newScreen = newScreen;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(@Nullable Screen screen) {
        this.newScreen = screen;
    }

    public @Nullable Screen getNewScreen() {
        return newScreen;
    }
}
