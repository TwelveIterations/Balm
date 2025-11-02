package net.blay09.mods.balm.world.level.block;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BalmBlockFactoryImpl implements BalmBlockFactory {

    private final BalmRegistrar registrar;
    private final String namespace;

    public BalmBlockFactoryImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public BalmBlockRegistration register(String name, Function<BlockBehaviour.Properties, Block> constructor, Supplier<BlockBehaviour.Properties> properties) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.BLOCK, resourceLocation);
        final var holder = registrar.register(resourceKey, (Supplier<Block>) () -> constructor.apply(properties.get().setId(resourceKey)));
        return new BalmBlockRegistrationImpl(registrar, holder);
    }

    @Override
    public <T> BalmDiscriminatedBlockRegistration<T> registerDiscriminated(Set<T> values, Function<T, String> nameFunction, BiFunction<T, BlockBehaviour.Properties, Block> constructor, Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesFunction) {
        final var map = new HashMap<T, BalmBlockRegistration>();
        for (final var value : values) {
            final var name = nameFunction.apply(value);
            final var registration = register(name, (properties) -> constructor.apply(value, properties), propertiesFunction);
            map.put(value, registration);
        }
        return new BalmDiscriminatedBlockRegistrationImpl<>(map);
    }

    private record BalmDiscriminatedBlockRegistrationImpl<T>(
            Map<T, BalmBlockRegistration> map) implements BalmDiscriminatedBlockRegistration<T>, DiscriminatedBlocks<T> {
        @Override
        public DeferredBlock getDeferred(T discriminator) {
            final var result = map.get(discriminator);
            if (result == null) {
                throw new IllegalArgumentException("Unknown block discriminator " + discriminator);
            }
            return result.asDeferredBlock();
        }

        @Override
        public Block get(T discriminator) {
            final var result = map.get(discriminator);
            if (result == null) {
                throw new IllegalArgumentException("Unknown block discriminator " + discriminator);
            }
            return result.asHolder().value();
        }

        @Override
        public Collection<DeferredBlock> allDeferred() {
            return map.values().stream().map(BalmBlockRegistration::asDeferredBlock).toList();
        }

        @Override
        public Collection<Block> all() {
            return map.values().stream().map(BalmBlockRegistration::asHolder).map(Holder::value).toList();
        }

        @Override
        public BalmDiscriminatedBlockRegistration<T> forEach(BiConsumer<T, BalmBlockRegistration> consumer) {
            map.forEach(consumer);
            return this;
        }

        @Override
        public DiscriminatedBlocks<T> asDiscriminatedBlocks() {
            return this;
        }
    }

    private static final class BalmBlockRegistrationImpl implements BalmBlockRegistration {
        private final BalmRegistrar registrar;
        private final Holder<Block> holder;
        private DeferredBlock deferredBlock;

        private BalmBlockRegistrationImpl(BalmRegistrar registrar, Holder<Block> holder) {
            this.registrar = registrar;
            this.holder = holder;
        }

        @Override
        public BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor, Supplier<Item.Properties> properties) {
            final var blockResourceKey = holder.unwrapKey().orElseThrow();
            final var block = holder.value();
            final var itemResourceKey = ResourceKey.create(Registries.ITEM, blockResourceKey.location());
            registrar.register(itemResourceKey, (Supplier<Item>) () -> constructor.apply(block, properties.get()));
            return this;
        }

        @Override
        public Holder<Block> asHolder() {
            return holder;
        }

        @Override
        public DeferredBlock asDeferredBlock() {
            if (deferredBlock == null) {
                deferredBlock = new DeferredBlockImpl(holder);
            }
            return deferredBlock;
        }
    }
}
