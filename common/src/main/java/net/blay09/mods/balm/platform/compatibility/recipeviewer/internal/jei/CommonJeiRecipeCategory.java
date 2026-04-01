package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerDisplaySlotsBuilder;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.function.BiConsumer;

public class CommonJeiRecipeCategory<T> implements IRecipeCategory<T> {

    private final IRecipeType<T> recipeType;
    private final Component title;
    private final @Nullable IDrawable icon;
    private final int width;
    private final int height;
    private final @Nullable IDrawable background;
    private final BiConsumer<T, RecipeViewerDisplaySlotsBuilder> slotsBuilder;

    public CommonJeiRecipeCategory(IRecipeType<T> recipeType, Component title, @Nullable IDrawable icon, int width, int height, @Nullable IDrawable background, BiConsumer<T, RecipeViewerDisplaySlotsBuilder> slotsBuilder) {
        this.recipeType = recipeType;
        this.title = title;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.background = background;
        this.slotsBuilder = slotsBuilder;
    }

    @Override
    public IRecipeType<T> getRecipeType() {
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
    public void draw(Object recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
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
