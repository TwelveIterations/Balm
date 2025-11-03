package net.blay09.mods.balm.api.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Use {@link Balm#blocks(String, Consumer)} instead.
 */
@Deprecated
public interface BalmBlocks {

    /**
     * Use {@link Balm#blocks(String, Consumer)} instead.
     */
    static BlockBehaviour.Properties blockProperties(ResourceLocation identifier) {
        return BlockBehaviour.Properties.of().setId(blockId(identifier));
    }

    /**
     * Use {@link Balm#blocks(String, Consumer)} instead.
     */
    static ResourceKey<Block> blockId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.BLOCK, identifier);
    }

    /**
     * Use {@link Balm#blocks(String, Consumer)} instead.
     */
    default DeferredObject<Block> registerBlock(Function<ResourceLocation, Block> constructor, ResourceLocation identifier) {
        final var resourceKey = ResourceKey.create(Registries.BLOCK, identifier);
        final var holder = Balm.registrar().register(resourceKey, constructor);
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * Use {@link Balm#blocks(String, Consumer)} instead.
     */
    default DeferredObject<Item> registerBlockItem(Function<ResourceLocation, BlockItem> constructor, ResourceLocation identifier, @Nullable ResourceLocation creativeTab){
        final var resourceKey = ResourceKey.create(Registries.ITEM, identifier);
        final var holder = Balm.registrar().register(resourceKey, constructor::apply);
        BalmItems.legacyCreativeModeTabItems.put(identifier.getNamespace(), resourceKey);
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * Use {@link Balm#blocks(String, Consumer)} instead.
     */
    default void register(Function<ResourceLocation, Block> blockSupplier, BiFunction<Block, ResourceLocation, BlockItem> blockItemSupplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        final var block = registerBlock(blockSupplier, identifier);
        registerBlockItem((id) -> blockItemSupplier.apply(block.get(), id), identifier, creativeTab);
    }

    /**
     * Use {@link Balm#blocks(String, Consumer)} instead.
     */
    default DeferredObject<Item> registerBlockItem(Function<ResourceLocation, BlockItem> supplier, ResourceLocation identifier) {
        return registerBlockItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * Use {@link Balm#blocks(String, Consumer)} instead.
     */
    default void register(Function<ResourceLocation, Block> blockSupplier, BiFunction<Block, ResourceLocation, BlockItem> blockItemSupplier, ResourceLocation identifier) {
        register(blockSupplier, blockItemSupplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * Use {@link Balm#blocks(String, Consumer)} instead.
     */
    default BalmBlocks scoped(String modId) {
        return this;
    }

    BalmBlocks LEGACY = new BalmBlocks() {
    };
}
