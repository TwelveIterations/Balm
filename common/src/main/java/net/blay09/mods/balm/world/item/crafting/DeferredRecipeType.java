package net.blay09.mods.balm.world.item.crafting;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface DeferredRecipeType<TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> {
    RecipeType<TRecipe> type();

    RecipeSerializer<TRecipe> serializer();

    default Optional<RecipeHolder<TRecipe>> getRecipeFor(Level level, TRecipeInput input) {
        return getRecipeFor(level, input, (RecipeHolder<TRecipe>) null);
    }

    Optional<RecipeHolder<TRecipe>> getRecipeFor(Level level, TRecipeInput input, ResourceKey<Recipe<?>> lastRecipe);

    Optional<RecipeHolder<TRecipe>> getRecipeFor(Level level, TRecipeInput input, RecipeHolder<TRecipe> lastRecipe);

}
