package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal;

import net.blay09.mods.balm.platform.compatibility.recipeviewer.RecipeViewerOcclusionProvider;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public record ScreenOcclusionRegistration<T extends AbstractContainerScreen<?>>(
        Class<T> containerScreenClass, RecipeViewerOcclusionProvider<T> provider) {
}
