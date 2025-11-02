package net.blay09.mods.balm.world.level.block;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record DeferredBlock(Holder<Block> block) implements BlockLike {
    @Override
    public Item asItem() {
        return block.value().asItem();
    }

    @Override
    public BlockState defaultBlockState() {
        return block.value().defaultBlockState();
    }
}
