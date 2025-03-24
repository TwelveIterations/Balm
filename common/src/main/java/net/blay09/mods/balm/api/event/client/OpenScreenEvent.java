package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class OpenScreenEvent extends BalmEvent {
    private Screen screen;
    private Screen newScreen;

    public OpenScreenEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.newScreen = screen;
    }

    @Nullable
    public Screen getNewScreen() {
        return newScreen;
    }
}
