package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerDisplaySlotsBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class CommonJeiRecipeCategory<T> implements IRecipeCategory<T> {
    private final RecipeType<T> recipeType;
    private final Component title;
    private final @Nullable IDrawable icon;
    private final int width;
    private final int height;
    private final @Nullable IDrawable background;
    private final BiConsumer<T, RecipeViewerDisplaySlotsBuilder> slotsBuilder;

    public CommonJeiRecipeCategory(RecipeType<T> recipeType, Component title, @Nullable IDrawable icon, int width, int height, @Nullable IDrawable background, BiConsumer<T, RecipeViewerDisplaySlotsBuilder> slotsBuilder) {
        this.recipeType = recipeType;
        this.title = title;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.background = background;
        this.slotsBuilder = slotsBuilder;
    }

    @Override
    public RecipeType<T> getRecipeType() {
        return recipeType;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        slotsBuilder.accept(recipe, new JeiRecipeViewerDisplaySlotsBuilder(builder));
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (background != null) {
            background.draw(guiGraphics);
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
