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
import java.util.function.Supplier;

public interface BalmBlocks {

    static BlockBehaviour.Properties blockProperties(ResourceLocation identifier) {
        return BlockBehaviour.Properties.of();
    }

    static ResourceKey<Block> blockId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.BLOCK, identifier);
    }

    DeferredObject<Block> registerBlock(Function<ResourceLocation, Block> supplier, ResourceLocation identifier);

    DeferredObject<Item> registerBlockItem(Function<ResourceLocation, BlockItem> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab);

    void register(Function<ResourceLocation, Block> blockSupplier, BiFunction<Block, ResourceLocation, BlockItem> blockItemSupplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab);

    default DeferredObject<Item> registerBlockItem(Function<ResourceLocation, BlockItem> supplier, ResourceLocation identifier) {
        return registerBlockItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    default void register(Function<ResourceLocation, Block> blockSupplier, BiFunction<Block, ResourceLocation, BlockItem> blockItemSupplier, ResourceLocation identifier) {
        register(blockSupplier, blockItemSupplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * @deprecated Use {@link #blockProperties(ResourceLocation)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default BlockBehaviour.Properties blockProperties() {
        return BlockBehaviour.Properties.of();
    }

    /**
     * @deprecated Use {@link #registerBlock(Function, ResourceLocation)} instead
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default DeferredObject<Block> registerBlock(Supplier<Block> supplier, ResourceLocation identifier) {
        return registerBlock((id) -> supplier.get(), identifier);
    }

    /**
     * @deprecated Use {@link #registerBlockItem(Function, ResourceLocation)} instead
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier) {
        return registerBlockItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * @deprecated Use {@link #registerBlockItem(Function, ResourceLocation, ResourceLocation)} instead
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default DeferredObject<Item> registerBlockItem(Supplier<BlockItem> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        return registerBlockItem((id) -> supplier.get(), identifier, creativeTab);
    }

    /**
     * @deprecated Use {@link #register(Function, BiFunction, ResourceLocation)} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier) {
        register(blockSupplier, blockItemSupplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * @deprecated Use {@link #register(Function, BiFunction, ResourceLocation, ResourceLocation)} instead
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default void register(Supplier<Block> blockSupplier, Supplier<BlockItem> blockItemSupplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        registerBlock(blockSupplier, identifier);
        registerBlockItem((id) -> blockItemSupplier.get(), identifier, creativeTab);
    }

    BalmBlocks scoped(String modId);
}
