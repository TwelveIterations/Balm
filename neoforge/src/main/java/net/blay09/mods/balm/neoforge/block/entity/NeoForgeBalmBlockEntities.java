package net.blay09.mods.balm.neoforge.block.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntityFactory;
import net.blay09.mods.balm.neoforge.DeferredRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.function.Supplier;

public class NeoForgeBalmBlockEntities implements BalmBlockEntities {
    @Override
    public <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation identifier, BalmBlockEntityFactory<T> factory, Supplier<Block[]> blocks) {
        final var register = DeferredRegisters.get(Registries.BLOCK_ENTITY_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), () -> {
            Block[] resolvedBlocks = blocks.get();
            return BlockEntityType.Builder.of(factory::create, resolvedBlocks).build(null);
        });
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }

    @Override
    public <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation identifier, BalmBlockEntityFactory<T> factory, DeferredObject<Block>... blocks) {
        final var register = DeferredRegisters.get(Registries.BLOCK_ENTITY_TYPE, identifier.getNamespace());
        final var registryObject = register.register(identifier.getPath(), () -> {
            Block[] resolvedBlocks = Arrays.stream(blocks).map(DeferredObject::get).toArray(Block[]::new);
            return BlockEntityType.Builder.of(factory::create, resolvedBlocks).build(null);
        });
        return new DeferredObject<>(identifier, registryObject, registryObject::isBound);
    }
}
