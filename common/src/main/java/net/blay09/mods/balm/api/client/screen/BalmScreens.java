package net.blay09.mods.balm.api.client.screen;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public interface BalmScreens {
    <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> void registerScreen(Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory);

    AbstractWidget addRenderableWidget(Screen screen, AbstractWidget widget);
}
