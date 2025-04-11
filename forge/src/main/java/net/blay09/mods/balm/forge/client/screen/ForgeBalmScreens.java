package net.blay09.mods.balm.forge.client.screen;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.balm.api.client.screen.BalmScreenFactory;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.forge.ModBusEventRegisters;
import net.blay09.mods.balm.mixin.ScreenAccessor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record ForgeBalmScreens(NamespaceResolver namespaceResolver) implements BalmScreens {

    private static <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> void registerScreenImmediate(Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory) {
        MenuScreens.register(type.get(), screenFactory::create);
    }

    @Override
    public <T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>> void registerScreen(Supplier<MenuType<? extends T>> type, BalmScreenFactory<T, S> screenFactory) {
        getActiveRegistrations().menuTypes.add(Pair.of(type::get, screenFactory));
    }

    @Override
    public BalmScreens scoped(String modId) {
        return new ForgeBalmScreens(new StaticNamespaceResolver(modId));
    }

    @Override
    public AbstractWidget addRenderableWidget(Screen screen, AbstractWidget widget) {
        ScreenAccessor accessor = ((ScreenAccessor) screen);
        accessor.balm_getChildren().add(widget);
        accessor.balm_getRenderables().add(widget);
        accessor.balm_getNarratables().add(widget);
        return widget;
    }

    private Registrations getActiveRegistrations() {
        return ModBusEventRegisters.getRegistrations(namespaceResolver.getDefaultNamespace(), Registrations.class);
    }

    private static class Registrations {
        public final List<Pair<Supplier<MenuType<?>>, BalmScreenFactory<?, ?>>> menuTypes = new ArrayList<>();

        @SubscribeEvent
        @SuppressWarnings({"rawtypes", "unchecked"})
        public void setupClient(FMLClientSetupEvent event) {
            for (final var entry : menuTypes) {
                registerScreenImmediate(entry.getFirst()::get, (BalmScreenFactory) entry.getSecond()); // I hate Java generics.
            }
        }
    }
}
