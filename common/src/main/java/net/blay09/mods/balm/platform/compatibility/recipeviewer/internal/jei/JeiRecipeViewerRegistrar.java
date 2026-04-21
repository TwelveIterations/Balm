package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.jei;

import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerOcclusionProvider;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerRecipeTypeRegistration;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerRegistrar;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerVanillaRecipeTypeRegistration;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.IngredientInfoRegistration;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.IdentifiableRecipeTypeTransferRegistration;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.ScreenOcclusionRegistration;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class JeiRecipeViewerRegistrar implements RecipeViewerRegistrar {

    private final List<JeiRecipeTypeRegistration<?>> recipeTypeRegistrations = Collections.synchronizedList(new ArrayList<>());
    private final List<IngredientInfoRegistration> ingredientInfoRegistrations = Collections.synchronizedList(new ArrayList<>());
    private final List<ScreenOcclusionRegistration<?>> screenOcclusions = Collections.synchronizedList(new ArrayList<>());
    private final List<RecipeViewerOcclusionProvider<?>> globalScreenOcclusions = Collections.synchronizedList(new ArrayList<>());
    private final List<IdentifiableRecipeTypeTransferRegistration<?>> identifiableRecipeTypeTransferRegistrations = Collections.synchronizedList(new ArrayList<>());

    @Override
    public <T> RecipeViewerRecipeTypeRegistration<T> registerCustomRecipeType(Identifier identifier, Class<T> recipeClass) {
        final var recipeTypeRegistration = new JeiRecipeTypeRegistration<>(identifier, recipeClass);
        recipeTypeRegistrations.add(recipeTypeRegistration);
        return recipeTypeRegistration;
    }

    @Override
    public <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput, TRecipe> registerRecipeType(Identifier identifier, Class<TRecipe> recipeClass) {
        final var recipeTypeRegistration = new JeiVanillaRecipeTypeRegistration<>(identifier, recipeClass);
        recipeTypeRegistrations.add(recipeTypeRegistration);
        return recipeTypeRegistration;
    }

    @Override
    public void registerIngredientInfo(ItemLike itemLike, Component description) {
        ingredientInfoRegistrations.add(new IngredientInfoRegistration(itemLike, description));
    }

    @Override
    public <T extends AbstractContainerScreen<?>> void registerScreenOcclusion(Class<T> screenClass, RecipeViewerOcclusionProvider<T> provider) {
        screenOcclusions.add(new ScreenOcclusionRegistration<>(screenClass, provider));
    }

    @Override
    public void registerGlobalScreenOcclusion(RecipeViewerOcclusionProvider<AbstractContainerScreen<?>> provider) {
        globalScreenOcclusions.add(provider);
    }

    @Override
    public <T extends AbstractContainerMenu> void registerRecipeTransferHandler(Class<T> menuClass, Holder<MenuType<T>> menuType, RecipeType<?> recipeType, int recipeSlotStart, int recipeSlotCount, int inventorySlotStart, int inventorySlotCount) {
        final var recipeTypeId = BuiltInRegistries.RECIPE_TYPE.getKey(recipeType);
        if (recipeTypeId != null) {
            identifiableRecipeTypeTransferRegistrations.add(new IdentifiableRecipeTypeTransferRegistration<>(menuClass, menuType, recipeTypeId, recipeSlotStart, recipeSlotCount, inventorySlotStart, inventorySlotCount));
        }
    }

    public Collection<JeiRecipeTypeRegistration<?>> getRecipeTypes() {
        return recipeTypeRegistrations;
    }

    public Collection<IngredientInfoRegistration> getIngredientInfoRegistrations() {
        return ingredientInfoRegistrations;
    }

    public Collection<ScreenOcclusionRegistration<?>> getScreenOcclusions() {
        return screenOcclusions;
    }

    public List<RecipeViewerOcclusionProvider<?>> getGlobalScreenOcclusions() {
        return globalScreenOcclusions;
    }

    public Collection<IdentifiableRecipeTypeTransferRegistration<?>> getIdentifiableRecipeTypeTransferRegistrations() {
        return identifiableRecipeTypeTransferRegistrations;
    }
}
