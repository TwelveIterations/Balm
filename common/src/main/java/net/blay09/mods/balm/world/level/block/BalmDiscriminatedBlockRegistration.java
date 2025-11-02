package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.*;

public interface BalmDiscriminatedBlockRegistration<T> {
    BalmDiscriminatedBlockRegistration<T> withNullDiscriminator();

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems() {
        return forEach(it -> it.withDefaultItem());
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(Function<Item.Properties, Item.Properties> propertiesBuilder) {
        return forEach(it -> it.withItem(BlockItem::new, propertiesBuilder));
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(BiFunction<T, Item.Properties, Item.Properties> propertiesBuilder) {
        return forEach((discriminator, it) -> it.withItem(BlockItem::new, propertiesBuilder.apply(discriminator, new Item.Properties())));
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(Supplier<Item.Properties> propertiesSupplier) {
        return forEach(it -> it.withItem(BlockItem::new, propertiesSupplier));
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(Item.Properties properties) {
        return forEach(it -> it.withItem(BlockItem::new, properties));
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor) {
        return forEach(it -> it.withItem(constructor, Function.identity()));
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, Function<Item.Properties, Item.Properties> propertiesBuilder) {
        return forEach(it -> it.withItem(constructor, propertiesBuilder.apply(new Item.Properties())));
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, BiFunction<T, Item.Properties, Item.Properties> propertiesBuilder) {
        return forEach((discrimination, it) -> it.withItem(constructor, () -> propertiesBuilder.apply(discrimination, new Item.Properties())));
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, Item.Properties properties) {
        return forEach(it -> it.withItem(constructor, () -> properties));
    }

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, Supplier<Item.Properties> properties) {
        return forEach(it -> it.withItem(constructor, properties));
    }

    default BalmDiscriminatedBlockRegistration<T> forEach(Consumer<BalmBlockRegistration> consumer) {
        return forEach((discriminator, it) -> consumer.accept(it));
    }

    BalmDiscriminatedBlockRegistration<T> forEach(BiConsumer<T, BalmBlockRegistration> consumer);

    DiscriminatedBlocks<T> asDiscriminatedBlocks();
}
