package net.blay09.mods.balm.platform.compatibility.recipeviewer;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

public interface RecipeViewerRegistrar {
    <T> RecipeViewerRecipeTypeRegistration<T> registerCustomRecipeType(Identifier identifier, Class<T> recipeClass);

    <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput, TRecipe> registerRecipeType(Identifier identifier, Class<TRecipe> recipeClass);

    <T extends AbstractContainerScreen<?>> void registerScreenOcclusion(Class<T> screenClass, RecipeViewerOcclusionProvider<T> provider);
}
