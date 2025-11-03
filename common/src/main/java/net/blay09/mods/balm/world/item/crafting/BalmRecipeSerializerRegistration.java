package net.blay09.mods.balm.world.item.crafting;

import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public interface BalmRecipeSerializerRegistration<TRecipe extends Recipe<?>> extends BalmHolderRegistration<RecipeSerializer<TRecipe>> {
}
