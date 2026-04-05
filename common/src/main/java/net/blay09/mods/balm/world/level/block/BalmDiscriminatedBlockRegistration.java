package net.blay09.mods.balm.world.level.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmDiscriminatedBlockRegistration<T> extends Map<@Nullable T, BalmBlockRegistration> {
    default BalmDiscriminatedBlockRegistration<T> withDefaultItems() {
        forEach((_, it) -> it.withDefaultItem());
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(Function<Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((_, it) -> it.withItem(BlockItem::new, propertiesBuilder));
        return this;
    }

    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(BiFunction<@Nullable T, Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((discriminator, it) -> it.withItem(BlockItem::new, (properties) -> propertiesBuilder.apply(discriminator, properties)));
        return this;
    }

    /**
     * @deprecated Use {@link #withDefaultItems(Function)} or {@link #withDefaultItems(BiFunction)} instead, so that Balm can apply appropriate default properties.
     */
    @Deprecated
    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(Supplier<Item.Properties> propertiesSupplier) {
        forEach((_, it) -> it.withItem(BlockItem::new, propertiesSupplier));
        return this;
    }

    /**
     * @deprecated Use {@link #withDefaultItems(Function)} or {@link #withDefaultItems(BiFunction)} instead, so that Balm can apply appropriate default properties.
     */
    @Deprecated
    default BalmDiscriminatedBlockRegistration<T> withDefaultItems(Item.Properties properties) {
        forEach((_, it) -> it.withItem(BlockItem::new, properties));
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

    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, BiFunction<@Nullable T, Item.Properties, Item.Properties> propertiesBuilder) {
        forEach((discrimination, it) -> it.withItem(constructor, (properties) -> propertiesBuilder.apply(discrimination, properties)));
        return this;
    }

    /**
     * @deprecated Use {@link #withItems(BiFunction, Function)} or {@link #withItems(BiFunction, BiFunction)} instead, so that Balm can apply appropriate default properties.
     */
    @Deprecated
    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, Item.Properties properties) {
        forEach((_, it) -> it.withItem(constructor, () -> properties));
        return this;
    }

    /**
     * @deprecated Use {@link #withItems(BiFunction, Function)} or {@link #withItems(BiFunction, BiFunction)} instead, so that Balm can apply appropriate default properties.
     */
    @Deprecated
    default BalmDiscriminatedBlockRegistration<T> withItems(BiFunction<Block, Item.Properties, BlockItem> constructor, Supplier<Item.Properties> properties) {
        forEach((_, it) -> it.withItem(constructor, properties));
        return this;
    }

    DiscriminatedBlocks<T> asDiscriminatedBlocks();
}
