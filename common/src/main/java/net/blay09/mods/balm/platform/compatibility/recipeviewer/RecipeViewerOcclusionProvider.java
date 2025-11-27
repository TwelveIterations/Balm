package net.blay09.mods.balm.platform.compatibility.recipeviewer;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;

import java.util.List;

public interface RecipeViewerOcclusionProvider<T extends AbstractContainerScreen<?>> {
    List<Rect2i> getOcclusions(T containerScreen);
}
