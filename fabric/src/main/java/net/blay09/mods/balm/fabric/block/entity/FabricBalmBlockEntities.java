package net.blay09.mods.balm.fabric.block.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntityFactory;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.function.Supplier;

public class FabricBalmBlockEntities implements BalmBlockEntities {
    @Override
    public <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation identifier, BalmBlockEntityFactory<T> factory, Supplier<Block[]> blocks) {
        return new DeferredObject<>(identifier, () -> {
            Block[] resolvedBlocks = blocks.get();
            BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory::create, resolvedBlocks).build();
            return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, identifier.toString(), type);
        }).resolveImmediately();
    }

    @Override
    public <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation identifier, BalmBlockEntityFactory<T> factory, DeferredObject<Block>... blocks) {
        return new DeferredObject<>(identifier, () -> {
            Block[] resolvedBlocks = Arrays.stream(blocks).map(DeferredObject::get).toArray(Block[]::new);
            BlockEntityType<T> type = FabricBlockEntityTypeBuilder.create(factory::create, resolvedBlocks).build();
            return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, identifier.toString(), type);
        }).resolveImmediately();
    }
}
