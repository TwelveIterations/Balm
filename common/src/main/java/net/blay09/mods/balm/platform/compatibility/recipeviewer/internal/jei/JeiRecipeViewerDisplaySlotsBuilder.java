package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerDisplaySlotBuilder;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerDisplaySlotsBuilder;

class JeiRecipeViewerDisplaySlotsBuilder implements RecipeViewerDisplaySlotsBuilder {
    private final IRecipeLayoutBuilder builder;

    public JeiRecipeViewerDisplaySlotsBuilder(IRecipeLayoutBuilder builder) {
        this.builder = builder;
    }

    @Override
    public RecipeViewerDisplaySlotBuilder inputSlot(int x, int y) {
        return new JeiRecipeViewerDisplaySlotBuilder(builder.addInputSlot(x, y));
    }

    @Override
    public RecipeViewerDisplaySlotBuilder outputSlot(int x, int y) {
        return new JeiRecipeViewerDisplaySlotBuilder(builder.addOutputSlot(x, y));
    }

    @Override
    public RecipeViewerDisplaySlotBuilder craftingStationSlot(int x, int y) {
        return new JeiRecipeViewerDisplaySlotBuilder(builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, x, y));
    }

    @Override
    public RecipeViewerDisplaySlotBuilder renderOnlySlot(int x, int y) {
        return new JeiRecipeViewerDisplaySlotBuilder(builder.addSlot(RecipeIngredientRole.RENDER_ONLY, x, y));
    }
}
