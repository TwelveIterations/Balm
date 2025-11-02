package net.blay09.mods.balm.world.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public record DeferredItem(Holder<Item> item) implements ItemLike {
    @Override
    public Item asItem() {
        return item.value();
    }
}
