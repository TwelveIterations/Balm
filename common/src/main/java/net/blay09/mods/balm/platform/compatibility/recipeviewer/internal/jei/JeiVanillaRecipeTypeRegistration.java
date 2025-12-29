package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.jei;

import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerVanillaRecipeTypeRegistration;
import net.blay09.mods.balm.world.item.crafting.DeferredRecipeType;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;

public class JeiVanillaRecipeTypeRegistration<TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> extends JeiRecipeTypeRegistration<TRecipe> implements RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput, TRecipe> {

    private final List<Holder<RecipeType<TRecipe>>> recipeTypeHolders = new ArrayList<>();
    private final List<DeferredRecipeType<TRecipeInput, TRecipe>> deferredRecipeTypes = new ArrayList<>();

    public JeiVanillaRecipeTypeRegistration(Identifier identifier, Class<TRecipe> recipeClass) {
        super(identifier, recipeClass);
    }

    @Override
    public RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput, TRecipe> withSyncedRecipes(Holder<RecipeType<TRecipe>> recipeType) {
        recipeTypeHolders.add(recipeType);
        return this;
    }

    @Override
    public RecipeViewerVanillaRecipeTypeRegistration<TRecipeInput, TRecipe> withSyncedRecipes(DeferredRecipeType<TRecipeInput, TRecipe> recipeType) {
        deferredRecipeTypes.add(recipeType);
        return this;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        super.registerRecipes(registration);

        Balm.safeClientAccess().getRecipeMap().ifPresent(recipeMap -> {
            for (final var recipeTypeHolder : recipeTypeHolders) {
                final var recipes = recipeMap.byType(recipeTypeHolder.value()).stream()
                        .map(RecipeHolder::value)
                        .toList();
                registration.addRecipes(jeiRecipeType, recipes);
            }

            for (final var deferredRecipeType : deferredRecipeTypes) {
                final var recipes = recipeMap.byType(deferredRecipeType.type()).stream()
                        .map(RecipeHolder::value)
                        .toList();
                registration.addRecipes(jeiRecipeType, recipes);
            }
        });
    }

    public boolean containsRecipeType(RecipeType<?> recipeType) {
        return recipeTypeHolders.stream().anyMatch(it -> it.value() == recipeType) || deferredRecipeTypes.stream().anyMatch(it -> it.type() == recipeType);
    }
}
