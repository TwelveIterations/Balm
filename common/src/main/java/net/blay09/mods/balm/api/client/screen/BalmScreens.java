package net.blay09.mods.balm.api.client.screen;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Deprecated
public interface BalmScreens {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#menuScreens(Consumer)} instead.
     */
    @Deprecated
    default <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> void registerScreen(ResourceLocation identifier, Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory) {
        registerScreen(type, screenFactory);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.gui.screens.BalmScreenUtils#addRenderableWidget(Screen, GuiEventListener)} instead.
     */
    @Deprecated
    AbstractWidget addRenderableWidget(Screen screen, AbstractWidget widget);

    /**
     * @deprecated Use {@link #registerScreen(ResourceLocation, Supplier, BalmScreenFactory)} instead.
     */
    @Deprecated
    <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> void registerScreen(Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory);

    @Deprecated
    BalmScreens scoped(String modId);
}
