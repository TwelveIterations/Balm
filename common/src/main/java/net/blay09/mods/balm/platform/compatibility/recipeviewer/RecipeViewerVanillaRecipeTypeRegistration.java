package net.blay09.mods.balm.platform.compatibility.recipeviewer;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

public interface RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> extends RecipeViewerRecipeTypeRegistration<TRecipe> {
    RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput, TRecipe> withSyncedRecipes(RecipeType<TRecipe> recipeType);
}
