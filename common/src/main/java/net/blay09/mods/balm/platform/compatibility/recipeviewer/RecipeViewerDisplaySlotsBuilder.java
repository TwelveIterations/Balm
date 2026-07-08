package net.blay09.mods.balm.platform.compatibility.recipeviewer;

public interface RecipeViewerDisplaySlotsBuilder {
    RecipeViewerDisplaySlotBuilder inputSlot(int x, int y);

    RecipeViewerDisplaySlotBuilder outputSlot(int x, int y);

    RecipeViewerDisplaySlotBuilder craftingStationSlot(int x, int y);

    RecipeViewerDisplaySlotBuilder renderOnlySlot(int x, int y);
}
