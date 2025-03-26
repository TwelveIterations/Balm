package net.blay09.mods.balm.api.event.client;

import net.blay09.mods.balm.api.event.BalmEvent;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.RecipeManager;

public class RecipesUpdatedEvent extends BalmEvent {
    private final RecipeManager recipeManager;
    private final RegistryAccess registryAccess;

    public RecipesUpdatedEvent(RecipeManager recipeManager, RegistryAccess registryAccess) {
        this.recipeManager = recipeManager;
        this.registryAccess = registryAccess;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public RegistryAccess getRegistryAccess() {
        return registryAccess;
    }
}
