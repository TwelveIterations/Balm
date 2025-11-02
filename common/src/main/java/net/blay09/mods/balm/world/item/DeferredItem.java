package net.blay09.mods.balm.world.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public interface DeferredItem extends ItemLike, Holder<Item> {
    default ItemStack createStack() {
        return createStack(1);
    }

    ItemStack createStack(int count);
}
