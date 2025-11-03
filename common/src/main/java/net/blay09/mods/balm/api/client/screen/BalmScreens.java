package net.blay09.mods.balm.api.client.screen;

import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @deprecated Use {@link BalmClient#menuScreens(String, Consumer)} instead.
 */
public interface BalmScreens {

    /**
     * @deprecated Use {@link BalmClient#menuScreens(String, Consumer)} instead.
     */
    default <TMenu extends AbstractContainerMenu, TScreen extends Screen & MenuAccess<TMenu>> void registerScreen(ResourceLocation id, Supplier<MenuType<? extends TMenu>> type, BalmScreenFactory<TMenu, TScreen> screenFactory) {
        BalmClient.menuScreens(id.getNamespace(), factory -> factory.register(id.getPath(), type, screenFactory));
    }

    /**
     * @deprecated Use an accessor or invoker mixin instead.
     */
    default AbstractWidget addRenderableWidget(Screen screen, AbstractWidget widget) {
        ScreenAccessor accessor = ((ScreenAccessor) screen);
        accessor.balm_getChildren().add(widget);
        accessor.balm_getRenderables().add(widget);
        accessor.balm_getNarratables().add(widget);
        return widget;
    }

    /**
     * @deprecated Use {@link BalmClient#menuScreens(String, Consumer)} instead.
     */
    default BalmScreens scoped(String modId) {
        return this;
    }

    BalmScreens LEGACY = new BalmScreens() {
    };
}
