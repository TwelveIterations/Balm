package net.blay09.mods.balm.neoforge.client.screen;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.client.screen.BalmScreenFactory;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class NeoForgeBalmScreens implements BalmScreens {

    private static class Registrations {
        public final List<Pair<Supplier<MenuType<?>>, BalmScreenFactory<?, ?>>> menuTypes = new ArrayList<>();

        @SubscribeEvent
        @SuppressWarnings({"unchecked", "rawtypes"})
        public void registerMenuScreens(RegisterMenuScreensEvent event) {
            for (Pair<Supplier<MenuType<? extends AbstractContainerMenu>>, BalmScreenFactory<?, ?>> entry : menuTypes) {
                final var menuType = entry.getFirst().get();
                final var screenFactory = entry.getSecond();
                registerScreenImmediate(event, (MenuType) menuType, (BalmScreenFactory) screenFactory);
            }
        }
    }

    private final Map<String, Registrations> registrations = new ConcurrentHashMap<>();

    @Override
    public <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> void registerScreen(ResourceLocation identifier, Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory) {
        getRegistrations(identifier.getNamespace()).menuTypes.add(Pair.of(type::get, screenFactory));
    }

    private static <TMenu extends AbstractContainerMenu, TScreen extends Screen & MenuAccess<TMenu>> void registerScreenImmediate(RegisterMenuScreensEvent event, MenuType<TMenu> type, BalmScreenFactory<TMenu, TScreen> screenFactory) {
        event.register(type, screenFactory::create);
    }

    @Override
    public AbstractWidget addRenderableWidget(Screen screen, AbstractWidget widget) {
        ScreenAccessor accessor = ((ScreenAccessor) screen);
        accessor.balm_getChildren().add(widget);
        accessor.balm_getRenderables().add(widget);
        accessor.balm_getNarratables().add(widget);
        return widget;
    }

    public void register(String modId, IEventBus eventBus) {
        eventBus.register(getRegistrations(modId));
    }

    private Registrations getRegistrations(String modId) {
        return registrations.computeIfAbsent(modId, it -> new Registrations());
    }
}
