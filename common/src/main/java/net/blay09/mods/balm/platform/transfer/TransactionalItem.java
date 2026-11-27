package net.blay09.mods.balm.platform.transfer;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record TransactionalItem(Item item, DataComponentPatch components) implements TransactionalResource {
    public TransactionalItem {
        if (item == Items.AIR) {
            components = DataComponentPatch.EMPTY;
        }
    }

    public static TransactionalItem of(ItemStack stack) {
        return new TransactionalItem(stack.getItem(), stack.getComponentsPatch());
    }

    public static TransactionalItem of(Item item) {
        return new TransactionalItem(item, DataComponentPatch.EMPTY);
    }

    public ItemStack toStack(int count) {
        return new ItemStack(BuiltInRegistries.ITEM.wrapAsHolder(item), count, components);
    }

    @Override
    public boolean isEmpty() {
        return item == Items.AIR;
    }
}
