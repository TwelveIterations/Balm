package net.blay09.mods.balm.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class SimpleRecipeType<T extends Recipe<?>> implements RecipeType<T> {

    private final ResourceLocation identifier;

    public SimpleRecipeType(ResourceLocation identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier.getPath();
    }
}
