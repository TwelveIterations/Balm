package net.blay09.mods.balm.world.item.crafting;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Function;

public class SimpleRecipeType<T extends Recipe<?>> implements RecipeType<T> {

    private final Identifier identifier;

    public SimpleRecipeType(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier.getPath();
    }

    public static <TRecipeInput extends RecipeInput, TRecipe extends Recipe<TRecipeInput>> Function<Identifier, SimpleRecipeType<TRecipe>> of(Class<TRecipe> clazz) {
        return SimpleRecipeType::new;
    }
}
