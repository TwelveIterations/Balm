package net.blay09.mods.balm.fabric.client.internal.config;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.List;

public final class FabricModListButton {
    private static final List<Component> BUTTON_ROW_CANDIDATES = List.of(
            Component.translatable("options.language"),
            Component.translatable("options.accessibility"),
            Component.translatable("gui.friends.open"),
            Component.translatable("menu.reportBugs"),
            Component.translatable("menu.sendFeedback"),
            Component.translatable("menu.playerReporting")
    );

    private FabricModListButton() {
    }

    public static void initialize() {
        if (!FabricLoader.getInstance().isModLoaded("modmenu")) {
            ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, _) -> addToScreen(client, screen, scaledWidth));
        }
    }

    private static void addToScreen(Minecraft client, Screen screen, int screenWidth) {
        final var iconButtons = locateButtonRow(screen);
        if (iconButtons.isEmpty()) {
            return;
        }

        final var button = SpriteIconButton.builder(Component.translatable("gui.balm.mods.button"),
                        _ -> client.gui.setScreen(new FabricModListScreen(screen)), true)
                .size(20, 20)
                .sprite(Identifier.fromNamespaceAndPath("balm", "widgets/mods"), 16, 16)
                .withTootip()
                .build();
        button.setY(iconButtons.getFirst().getY());
        final int count = iconButtons.size() + 1;
        for (int i = 0; i < iconButtons.size(); i++) {
            iconButtons.get(i).setX(getHorizontalPosition(screenWidth, i, count, 20));
        }
        button.setX(getHorizontalPosition(screenWidth, count - 1, count, 20));
        Screens.getWidgets(screen).add(button);
    }

    private static List<SpriteIconButton> locateButtonRow(Screen screen) {
        final var widgets = Screens.getWidgets(screen);
        final var anchor = widgets.stream()
                .filter(widget -> BUTTON_ROW_CANDIDATES.contains(widget.getMessage()))
                .findFirst();
        if (anchor.isEmpty()) {
            return List.of();
        }

        final int y = anchor.get().getY();
        return widgets.stream()
                .filter(widget -> widget instanceof SpriteIconButton button && button.getY() == y)
                .map(widget -> (SpriteIconButton) widget)
                .toList();
    }

    private static int getHorizontalPosition(int screenWidth, int currentButton, int numberOfButtons, int buttonWidth) {
        final int totalWidth = numberOfButtons * buttonWidth + (numberOfButtons - 1) * 4;
        return screenWidth / 2 - totalWidth / 2 + currentButton * (buttonWidth + 4);
    }
}
