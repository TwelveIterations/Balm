package net.blay09.mods.balm.world.level.block.entity;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.level.block.BlockLike;
import net.blay09.mods.balm.world.level.block.DiscriminatedBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public abstract class AbstractBalmBlockEntityTypeFactoryImpl implements BalmBlockEntityTypeFactory {

    private final BalmRegistrar registrar;
    private final String namespace;

    public AbstractBalmBlockEntityTypeFactoryImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public <T extends BlockEntity> BalmBlockEntityTypeRegistration<T> register(String name, BlockEntitySupplier<T> constructor, BlockLike... blocks) {
        return register(name, constructor, () -> {
            final var resolvedBlocks = new HashSet<Block>();
            for (final var blockLike : blocks) {
                resolvedBlocks.add(blockLike.asBlock());
            }
            return resolvedBlocks;
        });
    }

    @Override
    public <T extends BlockEntity> BalmBlockEntityTypeRegistration<T> register(String name, BlockEntitySupplier<T> constructor, DiscriminatedBlocks<?> blocks) {
        return register(name, constructor, () -> Set.copyOf(blocks.all()));
    }

    @Override
    public <T extends BlockEntity> BalmBlockEntityTypeRegistration<T> register(String name, BlockEntitySupplier<T> constructor, Supplier<Set<Block>> blocksSupplier) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, resourceLocation);
        final var holder = registrar.register(resourceKey, (Supplier<BlockEntityType<?>>) () -> createBlockEntityType(constructor, blocksSupplier.get()));
        return new BalmBlockEntityTypeRegistrationImpl<>(holder);
    }

    private static final class BalmBlockEntityTypeRegistrationImpl<T extends BlockEntity> implements BalmBlockEntityTypeRegistration<T>, Supplier<BlockEntityType<T>> {
        private final Holder<BlockEntityType<T>> holder;

        @SuppressWarnings("unchecked")
        private BalmBlockEntityTypeRegistrationImpl(Holder<?> holder) {
            this.holder = (Holder<BlockEntityType<T>>) holder;
        }

        @Override
        public Holder<BlockEntityType<T>> asHolder() {
            return holder;
        }

        @Override
        public BlockEntityType<T> get() {
            return holder.value();
        }

        @Override
        public Supplier<BlockEntityType<T>> asSupplier() {
            return this;
        }
    }
}
