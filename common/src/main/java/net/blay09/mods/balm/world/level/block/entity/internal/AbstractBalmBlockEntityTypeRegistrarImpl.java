package net.blay09.mods.balm.world.level.block.entity.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.level.block.BlockLike;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistration;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public abstract class AbstractBalmBlockEntityTypeRegistrarImpl implements BalmBlockEntityTypeRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    public AbstractBalmBlockEntityTypeRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public void addAlias(Identifier oldId, Identifier newId) {
        registrar.addAlias(
                Registries.BLOCK_ENTITY_TYPE,
                oldId,
                newId
        );
    }

    @Override
    public void addAlias(String oldName, String newName) {
        addAlias(
                Identifier.fromNamespaceAndPath(namespace, oldName),
                Identifier.fromNamespaceAndPath(namespace, newName)
        );
    }

    @Override
    public <T extends BlockEntity> BalmBlockEntityTypeRegistration<T> register(String name, BlockEntitySupplier<T> constructor, BlockLike... blocks) {
        return register(name, constructor, Set.of(blocks));
    }

    @Override
    public <T extends BlockEntity> BalmBlockEntityTypeRegistration<T> register(String name, BlockEntitySupplier<T> constructor, Iterable<? extends BlockLike> blocks) {
        return register(name, constructor, () -> {
            final var resolvedBlocks = new HashSet<Block>();
            for (final var blockLike : blocks) {
                resolvedBlocks.add(blockLike.asBlock());
            }
            return resolvedBlocks;
        });
    }

    @Override
    public <T extends BlockEntity> BalmBlockEntityTypeRegistration<T> register(String name, BlockEntitySupplier<T> constructor, Supplier<Set<Block>> blocksSupplier) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, identifier);
        final var holder = registrar.register(resourceKey, (id) -> createBlockEntityType(constructor, blocksSupplier.get()));
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
