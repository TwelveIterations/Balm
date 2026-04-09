package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.item.ItemStack;

public interface DeferredBlock extends BlockLike {
    default ItemStack createStack() {
        return createStack(1);
    }

    ItemStack createStack(int count);
}
