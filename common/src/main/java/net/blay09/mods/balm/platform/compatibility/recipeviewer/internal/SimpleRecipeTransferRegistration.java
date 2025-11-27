package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal;

import net.minecraft.core.Holder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeType;

public record SimpleRecipeTransferRegistration<T extends AbstractContainerMenu>(
        Class<T> menuClass,
        Holder<MenuType<T>> menuType,
        RecipeType<?> recipeType,
        int recipeSlotStart,
        int recipeSlotCount,
        int inventorySlotStart,
        int inventorySlotCount
) {
}
