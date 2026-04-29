package net.blay09.mods.balm.world.level.block.internal;

import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.minecraft.core.Holder;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public record DeferredBlockImpl(
        Holder<Block> holder,
        ResourceKey<Block> blockId,
        @Nullable ResourceKey<Item> itemId
) implements DeferredBlock {

    @Override
    public Item asItem() {
        return holder.value().asItem();
    }

    @Override
    public BlockState defaultBlockState() {
        return holder.value().defaultBlockState();
    }

    @Override
    public ItemStack createStack(int count) {
        final var itemStack = asItem().getDefaultInstance();
        itemStack.setCount(count);
        return itemStack;
    }

    @Override
    public ResourceKey<Block> asResourceKey() {
        return holder.unwrapKey().orElseThrow();
    }

    @Override
    public BlockItemId asBlockItemId() {
        if (itemId == null) {
            throw new IllegalStateException("Cannot use asBlockItemId when no item is registered with this DeferredBlock");
        }
        return BlockItemId.create(blockId.identifier(), itemId.identifier());
    }

    @Override
    public Block asBlock() {
        return holder.value();
    }

    @Override
    public Holder<Block> asHolder() {
        return holder;
    }

}
