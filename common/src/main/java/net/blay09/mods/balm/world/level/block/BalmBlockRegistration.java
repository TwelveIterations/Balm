package net.blay09.mods.balm.world.level.block;

import net.blay09.mods.balm.core.BalmHolderRegistration;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface BalmBlockRegistration extends BalmHolderRegistration<Block> {
    default BalmBlockRegistration withDefaultItem() {
        return withItem(BlockItem::new);
    }

    default BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor) {
        return withItem(constructor, Function.identity());
    }

    default BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor, Function<Item.Properties, Item.Properties> propertiesBuilder) {
        return withItem(constructor, propertiesBuilder.apply(new Item.Properties()));
    }

    BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor, Item.Properties properties);
}
