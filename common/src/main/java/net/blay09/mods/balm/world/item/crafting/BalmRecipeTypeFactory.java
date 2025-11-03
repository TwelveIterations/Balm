package net.blay09.mods.balm.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.function.Function;

/**
 * Provides convenience access to registering recipe types and serializers.
 */
public interface BalmRecipeTypeFactory {

    default <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeTypeRegistration<TRecipeInput, TRecipe> register(String name) {
        return register(name, SimpleRecipeType::new);
    }

    <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeTypeRegistration<TRecipeInput, TRecipe> register(String name, Function<ResourceLocation, RecipeType<TRecipe>> constructor);

    <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeSerializerRegistration<TRecipe> registerSerializer(String name, Function<ResourceLocation, RecipeSerializer<TRecipe>> constructor);

    BalmRecipeBookCategoryRegistration registerBookCategory(String name, Function<ResourceLocation, RecipeBookCategory> constructor);

    <T extends RecipeDisplay.Type<?>> BalmRecipeDisplayTypeRegistration<T> registerDisplayType(String name, Function<ResourceLocation, T> constructor);

    <T extends SlotDisplay.Type<?>> BalmSlotDisplayTypeRegistration<T> registerSlotDisplayType(String name, Function<ResourceLocation, T> constructor);
}
