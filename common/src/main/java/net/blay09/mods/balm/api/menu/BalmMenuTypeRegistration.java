package net.blay09.mods.balm.api.menu;

import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface BalmMenuTypeRegistration<T extends AbstractContainerMenu> extends BalmHolderRegistration<MenuType<T>> {
}
