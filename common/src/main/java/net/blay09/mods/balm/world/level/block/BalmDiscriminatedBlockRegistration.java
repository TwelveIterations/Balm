package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface BalmDiscriminatedBlockRegistration<T> extends Map<T, BalmBlockRegistration> {
    default BalmDiscriminatedBlockRegistration<T> withDefaultItems() {
        forEach((_, it) -> it.withDefaultItem());
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(Function<Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((_, it) -> it.withItem(BlockItem::new, propertiesBuilder));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(BiFunction<T, Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((discriminator, it) -> it.withItem(BlockItem::new, (properties) -> propertiesBuilder.apply(discriminator, properties)));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor) {
        forEach((_, it) -> it.withItem(constructor, Function.identity()));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, Function<Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((_, it) -> it.withItem(constructor, propertiesBuilder));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, BiFunction<T, Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((discrimination, it) -> it.withItem(constructor, (properties) -> propertiesBuilder.apply(discrimination, properties)));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(Function<T, String> nameFunction, BiFunction<Block, Item.Properties, BlockItem> constructor, BiFunction<T, Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((discrimination, it) -> it.withItem(nameFunction.apply(discrimination), constructor, (properties) -> propertiesBuilder.apply(discrimination, properties)));
        return this;
    }

    DiscriminatedBlocks<T> asDiscriminatedBlocks();
}
