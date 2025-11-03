package net.blay09.mods.balm.api.menu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface BalmMenuTypeFactory {
    <TMenu extends AbstractContainerMenu, TPayload> BalmMenuTypeRegistration<TMenu> register(String name, BalmMenuFactory<TMenu, TPayload> factory);

    <TMenu extends AbstractContainerMenu, TPayload> MenuType<TMenu> createMenuType(BalmMenuFactory<TMenu, TPayload> factory);
}
