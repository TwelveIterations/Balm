package net.blay09.mods.balm.world.item;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record DeferredItem(Holder<Item> holder) implements ItemLike, Holder<Item> {
    @Override
    public Item asItem() {
        return holder.value();
    }

    @Override
    public Item value() {
        return holder.value();
    }

    @Override
    public boolean isBound() {
        return holder.isBound();
    }

    @Override
    public boolean is(ResourceLocation resourceLocation) {
        return holder.is(resourceLocation);
    }

    @Override
    public boolean is(ResourceKey<Item> resourceKey) {
        return holder.is(resourceKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<Item>> predicate) {
        return holder.is(predicate);
    }

    @Override
    public boolean is(TagKey<Item> tagKey) {
        return holder.is(tagKey);
    }

    @Override
    public boolean is(Holder<Item> holder) {
        return holder.is(holder);
    }

    @Override
    public Stream<TagKey<Item>> tags() {
        return holder.tags();
    }

    @Override
    public Either<ResourceKey<Item>, Item> unwrap() {
        return holder.unwrap();
    }

    @Override
    public Optional<ResourceKey<Item>> unwrapKey() {
        return holder.unwrapKey();
    }

    @Override
    public Kind kind() {
        return holder.kind();
    }

    @Override
    public boolean canSerializeIn(HolderOwner<Item> holderOwner) {
        return holder.canSerializeIn(holderOwner);
    }
}
