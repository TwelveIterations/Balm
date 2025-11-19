package net.blay09.mods.balm.world.item.crafting;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.function.Function;

/**
 * Provides convenience access to registering recipe types and serializers.
 */
public interface BalmRecipeTypeRegistrar {

    default <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeTypeRegistration<TRecipeInput, TRecipe> register(String name, Class<TRecipe> recipeClass) {
        return register(name, SimpleRecipeType.of(recipeClass));
    }

    <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeTypeRegistration<TRecipeInput, TRecipe> register(String name, Function<Identifier, ? extends RecipeType<TRecipe>> constructor);

    <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeSerializerRegistration<TRecipe> registerSerializer(String name, Function<Identifier, RecipeSerializer<TRecipe>> constructor);

    BalmRecipeBookCategoryRegistration registerBookCategory(String name, Function<Identifier, RecipeBookCategory> constructor);

    <T extends RecipeDisplay.Type<?>> BalmRecipeDisplayTypeRegistration<T> registerDisplayType(String name, Function<Identifier, T> constructor);

    <T extends SlotDisplay.Type<?>> BalmSlotDisplayTypeRegistration<T> registerSlotDisplayType(String name, Function<Identifier, T> constructor);
}
