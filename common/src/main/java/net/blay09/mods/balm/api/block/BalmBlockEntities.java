package net.blay09.mods.balm.api.block;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.entity.BalmBlockEntityFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @deprecated Use {@link net.blay09.mods.balm.api.Balm#blockEntityTypes(String, Consumer)} instead.
 */
@Deprecated
public interface BalmBlockEntities {
    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.Balm#blockEntityTypes(String, Consumer)} instead.
     */
    @Deprecated
    default <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation identifier, BalmBlockEntityFactory<T> factory, Supplier<Block[]> blocks) {
        final var holder = Balm.getRuntime().blockEntityTypes(identifier.getNamespace()).register(identifier.getPath(), factory::create, () -> Set.of(blocks.get())).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.api.Balm#blockEntityTypes(String, Consumer)} instead.
     */
    @Deprecated
    @SuppressWarnings({"unchecked"})
    default <T extends BlockEntity> DeferredObject<BlockEntityType<T>> registerBlockEntity(ResourceLocation identifier, BalmBlockEntityFactory<T> factory, DeferredObject<Block>... blocks) {
        final var holder = Balm.getRuntime().blockEntityTypes(identifier.getNamespace()).register(identifier.getPath(), factory::create, () -> {
            final var resolvedBlocks = new HashSet<Block>();
            for (final var block : blocks) {
                resolvedBlocks.add(block.get());
            }
            return resolvedBlocks;
        }).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    BalmBlockEntities LEGACY = new BalmBlockEntities() {
    };
}
