package net.blay09.mods.balm.world.item.internal;

import net.blay09.mods.balm.world.item.DeferredItem;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record DeferredItemImpl(Holder<Item> holder) implements DeferredItem {
    @Override
    public Item asItem() {
        return holder.value();
    }

    @Override
    public ItemStack createStack(int count) {
        final var itemStack = asItem().getDefaultInstance();
        itemStack.setCount(count);
        return itemStack;
    }

    @Override
    public Holder<Item> asHolder() {
        return holder;
    }
}
