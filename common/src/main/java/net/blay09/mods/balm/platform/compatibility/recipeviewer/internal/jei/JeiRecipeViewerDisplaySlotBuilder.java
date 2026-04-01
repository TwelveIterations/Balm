package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal.jei;

import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerDisplaySlotBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

class JeiRecipeViewerDisplaySlotBuilder implements RecipeViewerDisplaySlotBuilder {
    private final IRecipeSlotBuilder builder;

    public JeiRecipeViewerDisplaySlotBuilder(IRecipeSlotBuilder builder) {
        this.builder = builder;
    }

    @Override
    public RecipeViewerDisplaySlotBuilder add(Ingredient ingredient) {
        builder.add(ingredient);
        return this;
    }

    @Override
    public RecipeViewerDisplaySlotBuilder add(ItemStack itemStack) {
        builder.add(itemStack);
        return this;
    }

    @Override
    public RecipeViewerDisplaySlotBuilder add(ItemStackTemplate itemStackTemplate) {
        builder.add(itemStackTemplate.create());
        return this;
    }

    @Override
    public RecipeViewerDisplaySlotBuilder add(ItemLike itemLike) {
        builder.add(itemLike);
        return this;
    }

    @Override
    public RecipeViewerDisplaySlotBuilder withSlotBackground() {
        builder.setStandardSlotBackground();
        return this;
    }

    @Override
    public RecipeViewerDisplaySlotBuilder withOutputSlotBackground() {
        builder.setOutputSlotBackground();
        return this;
    }
}
