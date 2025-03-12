package net.blay09.mods.balm.api.block;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface BalmBlocks {

    DeferredObject<Block> registerBlock(Function<ResourceLocation, Block> supplier, ResourceLocation identifier);

    DeferredObject<Item> registerBlockItem(Function<ResourceLocation, BlockItem> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab);

    void register(Function<ResourceLocation, Block> blockSupplier, BiFunction<Block, ResourceLocation, BlockItem> blockItemSupplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab);

    default DeferredObject<Item> registerBlockItem(Function<ResourceLocation, BlockItem> supplier, ResourceLocation identifier) {
        return registerBlockItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    default void register(Function<ResourceLocation, Block> blockSupplier, BiFunction<Block, ResourceLocation, BlockItem> blockItemSupplier, ResourceLocation identifier) {
        register(blockSupplier, blockItemSupplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    static BlockBehaviour.Properties blockProperties(ResourceLocation identifier) {
        return BlockBehaviour.Properties.of().setId(blockId(identifier));
    }

    static ResourceKey<Block> blockId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.BLOCK, identifier);
    }
}
