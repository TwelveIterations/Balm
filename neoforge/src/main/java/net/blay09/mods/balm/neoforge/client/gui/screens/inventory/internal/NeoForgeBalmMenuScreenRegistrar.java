package net.blay09.mods.balm.neoforge.client.gui.screens.inventory.internal;

import net.blay09.mods.balm.api.client.screen.BalmScreenFactory;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.function.Supplier;

public class NeoForgeBalmMenuScreenRegistrar implements BalmMenuScreenRegistrar {

    private final RegisterMenuScreensEvent event;

    public NeoForgeBalmMenuScreenRegistrar(RegisterMenuScreensEvent event) {
        this.event = event;
    }

    @Override
    public <TMenu extends AbstractContainerMenu, TScreen extends Screen & MenuAccess<TMenu>> void register(Holder<MenuType<TMenu>> menuTypeHolder, BalmScreenFactory<TMenu, TScreen> screenFactory) {
        event.register(menuTypeHolder.value(), screenFactory::create);
    }

    @Override
    public <TMenu extends AbstractContainerMenu, TScreen extends Screen & MenuAccess<TMenu>> void register(String name, Supplier<MenuType<? extends TMenu>> menuTypeSupplier, BalmScreenFactory<TMenu, TScreen> screenFactory) {
        event.register(menuTypeSupplier.get(), screenFactory::create);
    }
}
