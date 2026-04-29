package net.blay09.mods.balm.world.level.block;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public interface DeferredBlock extends BlockLike {
    default ItemStack createStack() {
        return createStack(1);
    }

    ItemStack createStack(int count);

    ResourceKey<Block> asResourceKey();

    BlockItemId asBlockItemId();
}
