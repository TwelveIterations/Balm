package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public record IdentifiableRecipeTypeTransferRegistration<T extends AbstractContainerMenu>(
        Class<T> menuClass,
        Holder<MenuType<T>> menuType,
        ResourceLocation recipeTypeId,
        int recipeSlotStart,
        int recipeSlotCount,
        int inventorySlotStart,
        int inventorySlotCount
) {
}
