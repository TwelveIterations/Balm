package net.blay09.mods.balm.world.item.crafting;

import net.minecraft.core.Holder;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public interface DeferredRecipeType<T extends Recipe<?>> {
    Holder<RecipeType<T>> type();
    Holder<RecipeSerializer<T>> serializer();
    Holder<RecipeBookCategory> bookCategory();
}
