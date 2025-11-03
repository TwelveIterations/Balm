package net.blay09.mods.balm.world.item.crafting;

import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public interface BalmRecipeTypeRegistration<TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> extends BalmHolderRegistration<RecipeType<TRecipe>> {

    BalmRecipeTypeRegistration<TRecipeInput, TRecipe> withSerializer(Supplier<RecipeSerializer<TRecipe>> constructor);

    BalmRecipeTypeRegistration<TRecipeInput, TRecipe> withRecipeBookCategory();

    DeferredRecipeType<TRecipeInput, TRecipe> asDeferredRecipeType();
}
