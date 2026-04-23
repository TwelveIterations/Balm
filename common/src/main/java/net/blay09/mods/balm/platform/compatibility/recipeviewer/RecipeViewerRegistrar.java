package net.blay09.mods.balm.platform.compatibility.recipeviewer;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

public interface RecipeViewerRegistrar {
    <T> RecipeViewerRecipeTypeRegistration<T> registerCustomRecipeType(Identifier identifier, Class<T> recipeClass);

    <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput, TRecipe> registerRecipeType(Identifier identifier, Class<TRecipe> recipeClass);

    void registerIngredientInfo(ItemLike itemLike, Component description);

    <T extends AbstractContainerScreen<?>> void registerScreenOcclusion(Class<T> screenClass, RecipeViewerOcclusionProvider<T> provider);

    void registerGlobalScreenOcclusion(RecipeViewerOcclusionProvider<AbstractContainerScreen<?>> provider);

    <T extends AbstractContainerMenu> void registerRecipeTransferHandler(Class<T> menuClass,
                                                                                 Holder<MenuType<T>> menuType,
                                                                                 RecipeType<?> recipeType,
                                                                                 int recipeSlotStart,
                                                                                 int recipeSlotCount,
                                                                                 int inventorySlotStart,
                                                                                 int inventorySlotCount);

}
