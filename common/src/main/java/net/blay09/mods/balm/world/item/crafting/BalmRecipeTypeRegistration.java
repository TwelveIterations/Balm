package net.blay09.mods.balm.world.item.crafting;

import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmRecipeTypeRegistration<T extends Recipe<?>> extends BalmHolderRegistration<RecipeType<T>> {

    BalmRecipeTypeRegistration<T> withSerializer(Supplier<RecipeSerializer<T>> constructor);

    BalmRecipeTypeRegistration<T> withRecipeBookCategory();

    DeferredRecipeType<T> asDeferredRecipeType();
}
