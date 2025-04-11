package net.blay09.mods.balm.neoforge.block;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public record NeoForgeBalmBlocks(NamespaceResolver namespaceResolver, BalmItems items) implements BalmBlocks {
    @Override
    public DeferredObject<Block> registerBlock(Function<ResourceLocation, Block> supplier, ResourceLocation identifier) {
        final var register = DeferredRegisters.get(Registries.BLOCK, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), supplier);
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
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
        return new NeoForgeBalmBlocks(new StaticNamespaceResolver(modId), items.scoped(modId));
    }
}
