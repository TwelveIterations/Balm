package net.blay09.mods.balm.world.level.block.entity;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.level.block.BlockLike;
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
    public <T extends BlockEntity> BalmBlockEntityTypeRegistration register(String name, BlockEntitySupplier<T> constructor, BlockLike... blocks) {
        return register(name, constructor, () -> {
            final var resolvedBlocks = new HashSet<Block>();
            for (final var blockLike : blocks) {
                resolvedBlocks.add(blockLike.asBlock());
            }
            return resolvedBlocks;
        });
    }

    @Override
    public <T extends BlockEntity> BalmBlockEntityTypeRegistration register(String name, BlockEntitySupplier<T> constructor, Supplier<Set<Block>> blocksSupplier) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, resourceLocation);
        final var holder = registrar.register(resourceKey, (Supplier<BlockEntityType<?>>) () -> createBlockEntityType(constructor, blocksSupplier.get()));
        return new BalmBlockEntityTypeRegistrationImpl(holder);
    }

    private static final class BalmBlockEntityTypeRegistrationImpl implements BalmBlockEntityTypeRegistration {
        private final Holder<BlockEntityType<?>> holder;

        private BalmBlockEntityTypeRegistrationImpl(Holder<BlockEntityType<?>> holder) {
            this.holder = holder;
        }

        @Override
        public Holder<BlockEntityType<?>> asHolder() {
            return holder;
        }
    }
}
