package net.blay09.mods.balm.platform.compatibility.recipeviewer;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.BiConsumer;

public interface RecipeViewerDisplayBuilder<T> {
    RecipeViewerDisplayBuilder<T> size(int width, int height);

    default RecipeViewerDisplayBuilder<T> background(Identifier texture) {
        return background(texture, 0, 0);
    }

    RecipeViewerDisplayBuilder<T> background(Identifier texture, int u, int v);

    default RecipeViewerDisplayBuilder<T> icon(ItemLike itemLike) {
        return icon(new ItemStack(itemLike));
    }

    RecipeViewerDisplayBuilder<T> icon(ItemStack itemStack);

    RecipeViewerDisplayBuilder<T> title(Component title);

    RecipeViewerDisplayBuilder<T> slots(BiConsumer<T, RecipeViewerDisplaySlotsBuilder> builder);
}
