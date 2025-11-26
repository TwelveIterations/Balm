package net.blay09.mods.balm.client.color.item;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.function.Supplier;

public interface BalmItemColorRegistrar {

    default void register(ItemColor color, ItemLike... items) {
        register(color, () -> items);
    }

    default void register(ItemColor color, Iterable<? extends ItemLike> items) {
        register(color, () -> {
            final var resolvedItems = new ArrayList<ItemLike>();
            for (final var itemLike : items) {
                resolvedItems.add(itemLike);
            }
            return resolvedItems.toArray(ItemLike[]::new);
        });
    }

    void register(ItemColor color, Supplier<ItemLike[]> items);
}
