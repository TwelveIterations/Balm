package net.blay09.mods.balm.platform.compatibility.recipeviewer;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;
import java.util.function.Consumer;

public interface RecipeViewerRecipeTypeRegistration<T> {
    default RecipeViewerRecipeTypeRegistration<T> withCraftingStation(ItemLike itemLike) {
        return withCraftingStation(new ItemStack(itemLike));
    }

    RecipeViewerRecipeTypeRegistration<T> withCraftingStation(ItemStack itemStack);

    RecipeViewerRecipeTypeRegistration<T> withRecipe(T recipe);

    RecipeViewerRecipeTypeRegistration<T> withRecipes(Collection<T> recipes);

    void buildDisplay(Consumer<RecipeViewerDisplayBuilder<T>> builder);
}
