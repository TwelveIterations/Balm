package net.blay09.mods.balm.forge.recipe;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeBalmRecipes implements BalmRecipes {

    public <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Function<ResourceLocation, RecipeType<T>> supplier, ResourceLocation identifier) {
        final var register = DeferredRegisters.get(ForgeRegistries.RECIPE_TYPES, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), () -> supplier.apply(identifier));
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

    @Override
    public <T extends Recipe<?>> DeferredObject<RecipeSerializer<T>> registerRecipeSerializer(Supplier<RecipeSerializer<T>> supplier, ResourceLocation identifier) {
        final var register = DeferredRegisters.get(ForgeRegistries.RECIPE_SERIALIZERS, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isPresent);
    }

}
