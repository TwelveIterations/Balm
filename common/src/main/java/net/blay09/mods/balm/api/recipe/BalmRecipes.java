package net.blay09.mods.balm.api.recipe;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(Consumer)} instead.
 */
@Deprecated
public interface BalmRecipes {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(Consumer)} instead.
     */
    @Deprecated
    <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Function<ResourceLocation, RecipeType<T>> supplier, ResourceLocation identifier);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(Consumer)} instead.
     */
    @Deprecated
    <T extends Recipe<?>> DeferredObject<RecipeSerializer<T>> registerRecipeSerializer(Supplier<RecipeSerializer<T>> supplier, ResourceLocation identifier);

    /**
     * @deprecated Use {@link #registerRecipeType(Function, ResourceLocation)} and {@link #registerRecipeSerializer(Supplier, ResourceLocation)} instead.
     */
    @Deprecated
    default <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Supplier<RecipeType<T>> typeSupplier, Supplier<RecipeSerializer<T>> serializerSupplier, ResourceLocation identifier) {
        registerRecipeSerializer(serializerSupplier, identifier);
        return registerRecipeType((id) -> typeSupplier.get(), identifier);
    }
}
