package net.blay09.mods.balm.client.screen;

import net.blay09.mods.balm.api.client.screen.BalmScreenFactory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public interface BalmMenuScreenRegistrar {

    <TMenu extends AbstractContainerMenu, TScreen extends Screen & MenuAccess<TMenu>> void register(Holder<MenuType<TMenu>> menuTypeHolder, BalmScreenFactory<TMenu, TScreen> screenFactory);

    <TMenu extends AbstractContainerMenu, TScreen extends Screen & MenuAccess<TMenu>> void register(String name, Supplier<MenuType<? extends TMenu>> menuTypeSupplier, BalmScreenFactory<TMenu, TScreen> screenFactory);
}
