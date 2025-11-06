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
 * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#menuScreens(String, Consumer)} instead.
 */
@Deprecated
public interface BalmScreens {

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#menuScreens(String, Consumer)} instead.
     */
    @Deprecated
    default <TMenu extends AbstractContainerMenu, TScreen extends Screen & MenuAccess<TMenu>> void registerScreen(ResourceLocation id, Supplier<MenuType<? extends TMenu>> type, BalmScreenFactory<TMenu, TScreen> screenFactory) {
        BalmClient.getRuntime().menuScreens(id.getNamespace(), registrar -> registrar.register(id.getPath(), type, screenFactory));
    }

    /**
     * @deprecated Use an accessor or invoker mixin instead.
     */
    @Deprecated
    default AbstractWidget addRenderableWidget(Screen screen, AbstractWidget widget) {
        ScreenAccessor accessor = ((ScreenAccessor) screen);
        accessor.balm_getChildren().add(widget);
        accessor.balm_getRenderables().add(widget);
        accessor.balm_getNarratables().add(widget);
        return widget;
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.client.BalmClientRegistrars#menuScreens(String, Consumer)} instead.
     */
    @Deprecated
    default BalmScreens scoped(String modId) {
        return this;
    }

    BalmScreens LEGACY = new BalmScreens() {
    };
}
