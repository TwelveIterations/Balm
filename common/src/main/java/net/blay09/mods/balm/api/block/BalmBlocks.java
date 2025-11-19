package net.blay09.mods.balm.api.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.item.BalmItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Use {@link net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)} instead.
 */
@Deprecated
public interface BalmBlocks {

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)} instead.
     */
    static BlockBehaviour.Properties blockProperties(Identifier identifier) {
        return BlockBehaviour.Properties.of().setId(blockId(identifier));
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)} instead.
     */
    static ResourceKey<Block> blockId(Identifier identifier) {
        return ResourceKey.create(Registries.BLOCK, identifier);
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)} instead.
     */
    default DeferredObject<Block> registerBlock(Function<Identifier, Block> constructor, Identifier identifier) {
        final var resourceKey = ResourceKey.create(Registries.BLOCK, identifier);
        final var holder = Balm.getRuntime().registrar().register(resourceKey, constructor);
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)} instead.
     */
    default DeferredObject<Item> registerBlockItem(Function<Identifier, BlockItem> constructor, Identifier identifier, @Nullable Identifier creativeTab){
        final var resourceKey = ResourceKey.create(Registries.ITEM, identifier);
        final var holder = Balm.getRuntime().registrar().register(resourceKey, constructor::apply);
        BalmItems.legacyCreativeModeTabItems.put(identifier.getNamespace(), resourceKey);
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)} instead.
     */
    default void register(Function<Identifier, Block> blockSupplier, BiFunction<Block, Identifier, BlockItem> blockItemSupplier, Identifier identifier, @Nullable Identifier creativeTab) {
        final var block = registerBlock(blockSupplier, identifier);
        registerBlockItem((id) -> blockItemSupplier.apply(block.get(), id), identifier, creativeTab);
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)} instead.
     */
    default DeferredObject<Item> registerBlockItem(Function<Identifier, BlockItem> supplier, Identifier identifier) {
        return registerBlockItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)} instead.
     */
    default void register(Function<Identifier, Block> blockSupplier, BiFunction<Block, Identifier, BlockItem> blockItemSupplier, Identifier identifier) {
        register(blockSupplier, blockItemSupplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#blocks(String, Consumer)} instead.
     */
    default BalmBlocks scoped(String modId) {
        return this;
    }

    BalmBlocks LEGACY = new BalmBlocks() {
    };
}
