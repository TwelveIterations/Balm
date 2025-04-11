package net.blay09.mods.balm.fabric.block;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public record FabricBalmBlocks(NamespaceResolver namespaceResolver, BalmItems items) implements BalmBlocks {
    @Override
    public DeferredObject<Block> registerBlock(Function<ResourceLocation, Block> supplier, ResourceLocation identifier) {
        return new DeferredObject<>(identifier, () -> {
            final var block = supplier.apply(identifier);
            return Registry.register(BuiltInRegistries.BLOCK, identifier, block);
        }).resolveImmediately();
    }

    @Override
    public DeferredObject<Item> registerBlockItem(Function<ResourceLocation, BlockItem> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        return items.registerItem(supplier::apply, identifier, creativeTab);
    }

    @Override
    public void register(Function<ResourceLocation, Block> blockSupplier, BiFunction<Block, ResourceLocation, BlockItem> blockItemSupplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        final var deferredBlock = registerBlock(blockSupplier, identifier);
        registerBlockItem((id) -> blockItemSupplier.apply(deferredBlock.get(), id), identifier, creativeTab);
    }

    @Override
    public BalmBlocks scoped(String modId) {
        return new FabricBalmBlocks(new StaticNamespaceResolver(modId), items.scoped(modId));
    }
}
