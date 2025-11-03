package net.blay09.mods.balm.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.function.Function;

/**
 * Provides convenience access to registering recipe types and serializers.
 */
public interface BalmRecipeTypeFactory {

    <T extends Recipe<?>> BalmRecipeTypeRegistration<T> register(String name, Function<ResourceLocation, RecipeType<T>> constructor);

    <T extends Recipe<?>> BalmRecipeSerializerRegistration<T> registerSerializer(String name, Function<ResourceLocation, RecipeSerializer<T>> constructor);

    BalmRecipeBookCategoryRegistration registerBookCategory(String name, Function<ResourceLocation, RecipeBookCategory> constructor);

    <T extends RecipeDisplay.Type<?>> BalmRecipeDisplayTypeRegistration<T> registerDisplayType(String name, Function<ResourceLocation, T> constructor);

    <T extends SlotDisplay.Type<?>> BalmSlotDisplayTypeRegistration<T> registerSlotDisplayType(String name, Function<ResourceLocation, T> constructor);
}
