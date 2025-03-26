package net.blay09.mods.balm.api.menu;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface BalmMenus {
    <T extends AbstractContainerMenu> DeferredObject<MenuType<T>> registerMenu(ResourceLocation identifier, BalmMenuFactory<T> factory);
}
