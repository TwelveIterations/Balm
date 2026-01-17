package net.blay09.mods.balm.world.level.block.internal;

import com.mojang.datafixers.util.Either;
import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record DeferredBlockImpl(Holder<Block> holder) implements DeferredBlock {
    @Override
    public Item asItem() {
        return holder.value().asItem();
    }

    @Override
    public BlockState defaultBlockState() {
        return holder.value().defaultBlockState();
    }

    @Override
    public Block value() {
        return holder.value();
    }

    @Override
    public boolean isBound() {
        return holder.isBound();
    }

    @Override
    public boolean areComponentsBound() {
        return holder.areComponentsBound();
    }

    @Override
    public boolean is(Identifier identifier) {
        return holder.is(identifier);
    }

    @Override
    public boolean is(ResourceKey<Block> resourceKey) {
        return holder.is(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<Block>> predicate) {
        return holder.is(predicate);
    }

    @Override
    public boolean is(TagKey<Block> tagKey) {
        return holder.is(tagKey);
    }

    @Override
    public boolean is(Holder<Block> holder) {
        return holder.is(holder);
    }

    @Override
    public Stream<TagKey<Block>> tags() {
        return holder.tags();
    }

    @Override
    public DataComponentMap components() {
        return holder.components();
    }

    @Override
    public Either<ResourceKey<Block>, Block> unwrap() {
        return holder.unwrap();
    }

    @Override
    public Optional<ResourceKey<Block>> unwrapKey() {
        return holder.unwrapKey();
    }

    @Override
    public Kind kind() {
        return holder.kind();
    }

    @Override
    public boolean canSerializeIn(HolderOwner<Block> holderOwner) {
        return holder.canSerializeIn(holderOwner);
    }

    @Override
    public ItemStack createStack(int count) {
        final var itemStack = asItem().getDefaultInstance();
        itemStack.setCount(count);
        return itemStack;
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
