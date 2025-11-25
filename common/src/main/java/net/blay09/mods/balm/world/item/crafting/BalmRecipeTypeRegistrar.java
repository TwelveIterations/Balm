package net.blay09.mods.balm.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;

import java.util.function.Function;

/**
 * Provides convenience access to registering recipe types and serializers.
 */
public interface BalmRecipeTypeRegistrar {

    default <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeTypeRegistration<TRecipeInput, TRecipe> register(String name, Class<TRecipe> recipeClass) {
        return register(name, SimpleRecipeType.of(recipeClass));
    }

    <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeTypeRegistration<TRecipeInput, TRecipe> register(String name, Function<ResourceLocation, ? extends RecipeType<TRecipe>> constructor);

    <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> BalmRecipeSerializerRegistration<TRecipe> registerSerializer(String name, Function<ResourceLocation, RecipeSerializer<TRecipe>> constructor);

}
