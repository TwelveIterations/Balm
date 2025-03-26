package net.blay09.mods.balm.api.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class EmptyContainer implements ImplementedContainer {
    public static final EmptyContainer INSTANCE = new EmptyContainer();

    @Override
    public NonNullList<ItemStack> getItems() {
        return NonNullList.createWithCapacity(0);
    }
}
