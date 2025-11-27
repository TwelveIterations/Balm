package net.blay09.mods.balm.platform.compatibility.recipeviewer;

import net.blay09.mods.balm.world.item.crafting.DeferredRecipeType;
import net.minecraft.core.Holder;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

public interface RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> extends RecipeViewerRecipeTypeRegistration<TRecipe> {
    RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput, TRecipe> withSyncedRecipes(Holder<RecipeType<TRecipe>> recipeType);

    RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput, TRecipe> withSyncedRecipes(DeferredRecipeType<TRecipeInput, TRecipe> recipeType);
}
