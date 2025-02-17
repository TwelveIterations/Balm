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

import java.util.function.Supplier;

public interface BalmBlocks {
    BlockBehaviour.Properties blockProperties();

    DeferredObject<Block> registerBlock(Supplier<Block> supplier, ResourceLocation identifier);

    default DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier) {
        return registerBlockItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab);

    default void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier) {
        register(blockSupplier, blockItemSupplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab);

    static BlockBehaviour.Properties blockProperties(ResourceLocation identifier) {
        return BlockBehaviour.Properties.of();
    }

    static ResourceKey<Block> blockId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.BLOCK, identifier);
    }
}
