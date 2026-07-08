package net.blay09.mods.balm.platform.compatibility.recipeviewer;

import net.minecraft.resources.ResourceLocation;

public interface BalmModSupportRecipeViewer {
    void register(ResourceLocation identifier, RecipeViewerInfoProvider provider);

    boolean hasKeyboardFocus();
}
