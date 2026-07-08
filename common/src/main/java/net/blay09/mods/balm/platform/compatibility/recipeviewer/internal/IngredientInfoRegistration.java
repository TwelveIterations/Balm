package net.blay09.mods.balm.platform.compatibility.recipeviewer.internal;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;

public record IngredientInfoRegistration(ItemLike itemLike, Component description) {
}
