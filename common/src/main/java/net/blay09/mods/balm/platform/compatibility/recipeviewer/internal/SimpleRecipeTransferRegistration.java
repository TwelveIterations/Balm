package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal;

import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerRecipeTypeRegistration;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Predicate;

/**
 * @deprecated This class was nonsense and can't really be used for third party recipe types. Use {@link IdentifiableRecipeTypeTransferRegistration} instead.
 */
@Deprecated
public record SimpleRecipeTransferRegistration<T extends AbstractContainerMenu>(
        Class<T> menuClass,
        Holder<MenuType<T>> menuType,
        Predicate<RecipeViewerRecipeTypeRegistration<?>> recipeTypePredicate,
        int recipeSlotStart,
        int recipeSlotCount,
        int inventorySlotStart,
        int inventorySlotCount
) {
}
