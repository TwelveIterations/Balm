package net.blay09.mods.balm.world.level.block;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public interface DeferredBlock extends BlockLike, Holder<Block> {
    default ItemStack createStack() {
        return createStack(1);
    }

    ItemStack createStack(int count);
}
