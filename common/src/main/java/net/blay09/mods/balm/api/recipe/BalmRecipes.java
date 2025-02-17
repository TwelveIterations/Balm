package net.blay09.mods.balm.api.recipe;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmRecipes {
    /**
     * @deprecated Use {@link #registerRecipeType(Function, ResourceLocation)} and {@link #registerRecipeSerializer(Supplier, ResourceLocation)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.22")
    default <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Supplier<RecipeType<T>> typeSupplier, Supplier<RecipeSerializer<T>> serializerSupplier, ResourceLocation identifier) {
        registerRecipeSerializer(serializerSupplier, identifier);
        return registerRecipeType((id) -> typeSupplier.get(), identifier);
    }

    <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Function<ResourceLocation, RecipeType<T>> supplier, ResourceLocation identifier);

    <T extends Recipe<?>> DeferredObject<RecipeSerializer<T>> registerRecipeSerializer(Supplier<RecipeSerializer<T>> supplier, ResourceLocation identifier);

    <T extends RecipeDisplay.Type<?>> DeferredObject<T> registerRecipeDisplayType(Supplier<T> supplier, ResourceLocation identifier);

    <T extends SlotDisplay.Type<?>> DeferredObject<T> registerSlotDisplayType(Supplier<T> supplier, ResourceLocation identifier);

    DeferredObject<RecipeBookCategory> registerRecipeBookCategory(Supplier<RecipeBookCategory> supplier, ResourceLocation identifier);
}
