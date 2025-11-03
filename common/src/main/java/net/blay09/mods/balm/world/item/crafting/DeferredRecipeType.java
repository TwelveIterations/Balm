package net.blay09.mods.balm.world.item.crafting;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Optional;

public interface DeferredRecipeType<TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> {
    Holder<RecipeType<TRecipe>> type();

    Holder<RecipeSerializer<TRecipe>> serializer();

    Holder<RecipeBookCategory> bookCategory();

    default Optional<RecipeHolder<TRecipe>> getRecipeFor(Level level, TRecipeInput input) {
        return getRecipeFor(level, input, (RecipeHolder<TRecipe>) null);
    }

    Optional<RecipeHolder<TRecipe>> getRecipeFor(Level level, TRecipeInput input, @Nullable ResourceKey<Recipe<?>> lastRecipe);

    Optional<RecipeHolder<TRecipe>> getRecipeFor(Level level, TRecipeInput input, @Nullable RecipeHolder<TRecipe> lastRecipe);

}
