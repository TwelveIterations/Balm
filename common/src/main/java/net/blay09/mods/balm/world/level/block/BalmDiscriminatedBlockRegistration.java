package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Map;
import java.util.function.*;

public interface BalmDiscriminatedBlockRegistration<T> extends Map<T, BalmBlockRegistration> {
    default BalmDiscriminatedBlockRegistration<T> withDefaultItems() {
        forEach((discriminator, it) -> it.withDefaultItem());
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(Function<Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((discriminator, it) -> it.withItem(BlockItem::new, propertiesBuilder));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(BiFunction<T, Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((discriminator, it) -> it.withItem(BlockItem::new, () -> propertiesBuilder.apply(discriminator, new Item.Properties())));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(Supplier<Item.Properties> propertiesSupplier) {
        forEach((discriminator, it) -> it.withItem(BlockItem::new, propertiesSupplier));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(Item.Properties properties) {
        forEach((discriminator, it) -> it.withItem(BlockItem::new, properties));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor) {
        forEach((discriminator, it) -> it.withItem(constructor, Function.identity()));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, Function<Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((discriminator, it) -> it.withItem(constructor, () -> propertiesBuilder.apply(new Item.Properties())));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, BiFunction<T, Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((discrimination, it) -> it.withItem(constructor, () -> propertiesBuilder.apply(discrimination, new Item.Properties())));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, Item.Properties properties) {
        forEach((discriminator, it) -> it.withItem(constructor, () -> properties));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, Supplier<Item.Properties> properties) {
        forEach((discriminator, it) -> it.withItem(constructor, properties));
        return this;
    }

    DiscriminatedBlocks<T> asDiscriminatedBlocks();
}
