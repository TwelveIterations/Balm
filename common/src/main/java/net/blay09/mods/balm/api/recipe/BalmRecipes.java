package net.blay09.mods.balm.api.recipe;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(String, Consumer)} instead.
 */
@Deprecated
public interface BalmRecipes {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(String, Consumer)} instead.
     */
    @Deprecated
    default <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> DeferredObject<RecipeType<TRecipe>> registerRecipeType(Function<Identifier, ? extends RecipeType<TRecipe>> supplier, Identifier identifier) {
        // TODO no instance(s) of type variable(s) TRecipe, TRecipeInput exist so that ? can be converted to TRecipeInput
        final var holder = Balm.getRuntime().recipeTypes(identifier.getNamespace()).register(identifier.getPath(), id -> supplier.apply(id)).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(String, Consumer)} instead.
     */
    @Deprecated
    default <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> DeferredObject<RecipeSerializer<TRecipe>> registerRecipeSerializer(Supplier<RecipeSerializer<TRecipe>> supplier, Identifier identifier) {
        // TODO no instance(s) of type variable(s) TRecipe, TRecipeInput exist so that ? can be converted to TRecipeInput
        final var holder = Balm.getRuntime().recipeTypes(identifier.getNamespace()).registerSerializer(identifier.getPath(), id -> supplier.get()).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(String, Consumer)} instead.
     */
    @Deprecated
    default <T extends RecipeDisplay.Type<?>> DeferredObject<T> registerRecipeDisplayType(Supplier<T> supplier, Identifier identifier) {
        final var holder = Balm.getRuntime().recipeTypes(identifier.getNamespace()).registerDisplayType(identifier.getPath(), id -> supplier.get()).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(String, Consumer)} instead.
     */
    @Deprecated
    default <T extends SlotDisplay.Type<?>> DeferredObject<T> registerSlotDisplayType(Supplier<T> supplier, Identifier identifier) {
        final var holder = Balm.getRuntime().recipeTypes(identifier.getNamespace()).registerSlotDisplayType(identifier.getPath(), id -> supplier.get()).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#recipeTypes(String, Consumer)} instead.
     */
    @Deprecated
    default DeferredObject<RecipeBookCategory> registerRecipeBookCategory(Supplier<RecipeBookCategory> supplier, Identifier identifier) {
        final var holder = Balm.getRuntime().recipeTypes(identifier.getNamespace()).registerBookCategory(identifier.getPath(), id -> supplier.get()).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    BalmRecipes LEGACY = new BalmRecipes() {
    };
}
