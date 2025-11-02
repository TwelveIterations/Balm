package net.blay09.mods.balm.world.level.block;

import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmBlockRegistration extends BalmHolderRegistration<Block> {
    default BalmBlockRegistration withDefaultItem() {
        return withItem(BlockItem::new);
    }

    default BalmBlockRegistration withDefaultItem(Function<Item.Properties, Item.Properties> propertiesBuilder) {
        return withItem(BlockItem::new, propertiesBuilder);
    }

    default BalmBlockRegistration withDefaultItem(Supplier<Item.Properties> propertiesSupplier) {
        return withItem(BlockItem::new, propertiesSupplier);
    }

    default BalmBlockRegistration withDefaultItem(Item.Properties properties) {
        return withItem(BlockItem::new, properties);
    }

    default BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor) {
        return withItem(constructor, Function.identity());
    }

    default BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor, Function<Item.Properties, Item.Properties> propertiesBuilder) {
        return withItem(constructor, propertiesBuilder.apply(new Item.Properties()));
    }

    default BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor, Item.Properties properties) {
        return withItem(constructor, () -> properties);
    }

    BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor, Supplier<Item.Properties> properties);

    default BlockLike asBlockLike() {
        return asDeferredBlock();
    }

    default ItemLike asItemLike() {
        return asDeferredBlock();
    }

    DeferredBlock asDeferredBlock();
}
