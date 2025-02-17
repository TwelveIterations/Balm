package net.blay09.mods.balm.fabric.recipe;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class FabricBalmRecipes implements BalmRecipes {

    @Override
    public <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Supplier<RecipeType<T>> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> Registry.register(BuiltInRegistries.RECIPE_TYPE, identifier, supplier.get())).resolveImmediately();
    }

    @Override
    public <T extends Recipe<?>> DeferredObject<RecipeSerializer<T>> registerRecipeSerializer(Supplier<RecipeSerializer<T>> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, identifier, supplier.get())).resolveImmediately();
    }

}
