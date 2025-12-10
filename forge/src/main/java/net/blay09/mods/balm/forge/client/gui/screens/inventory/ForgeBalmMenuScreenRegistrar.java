package net.blay09.mods.balm.forge.client.gui.screens.inventory;

import net.blay09.mods.balm.client.gui.screens.BalmScreenFactory;
import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class ForgeBalmMenuScreenRegistrar implements BalmMenuScreenRegistrar {

    public static final BalmMenuScreenRegistrar INSTANCE = new ForgeBalmMenuScreenRegistrar();

    @Override
    public <TMenu extends AbstractContainerMenu, TScreen extends Screen & MenuAccess<TMenu>> void register(Holder<MenuType<TMenu>> menuTypeHolder, BalmScreenFactory<TMenu, TScreen> screenFactory) {
        MenuScreens.register(menuTypeHolder.value(), screenFactory::create);
    }

    @Override
    public <TMenu extends AbstractContainerMenu, TScreen extends Screen & MenuAccess<TMenu>> void register(String name, Supplier<MenuType<? extends TMenu>> menuTypeSupplier, BalmScreenFactory<TMenu, TScreen> screenFactory) {
        MenuScreens.register(menuTypeSupplier.get(), screenFactory::create);
    }
}
