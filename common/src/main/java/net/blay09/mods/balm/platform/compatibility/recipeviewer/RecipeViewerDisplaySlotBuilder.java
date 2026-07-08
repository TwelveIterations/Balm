package net.blay09.mods.balm.platform.compatibility.recipeviewer;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public interface RecipeViewerDisplaySlotBuilder {
    RecipeViewerDisplaySlotBuilder add(Ingredient ingredient);

    RecipeViewerDisplaySlotBuilder add(ItemStack itemStack);

    RecipeViewerDisplaySlotBuilder add(ItemLike itemLike);

    RecipeViewerDisplaySlotBuilder withSlotBackground();

    RecipeViewerDisplaySlotBuilder withOutputSlotBackground();
}
