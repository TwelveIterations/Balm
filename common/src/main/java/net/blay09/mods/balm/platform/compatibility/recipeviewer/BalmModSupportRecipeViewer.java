package net.blay09.mods.balm.platform.compatibility.recipeviewer;

import net.minecraft.resources.Identifier;

public interface BalmModSupportRecipeViewer {
    void register(Identifier identifier, RecipeViewerInfoProvider provider);
}
