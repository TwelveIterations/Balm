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
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.*;

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
    public <T> BalmDiscriminatedBlockRegistration<T> registerDiscriminated(Set<T> values, Function<T, String> nameFunction, BiFunction<T, BlockBehaviour.Properties, Block> constructor, BiFunction<T, BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesFunction) {
        final var map = new HashMap<T, BalmBlockRegistration>();
        for (final var value : values) {
            final var name = nameFunction.apply(value);
            final var registration = register(name, (properties) -> constructor.apply(value, properties), properties -> propertiesFunction.apply(value, properties));
            map.put(value, registration);
        }
        return new BalmDiscriminatedBlockRegistrationImpl<>(map, this, nameFunction, constructor, propertiesFunction);
    }

    private static class DiscriminatedBlocksImpl<T> implements DiscriminatedBlocks<T> {
        private final Map<T, BalmBlockRegistration> map;

        private DiscriminatedBlocksImpl(Map<T, BalmBlockRegistration> map) {
            this.map = map;
        }

        private BalmBlockRegistration getRegistration(@Nullable T discriminator) {
            final var result = map.get(discriminator);
            if (result == null) {
                throw new IllegalArgumentException("Unknown block discriminator " + discriminator);
            }
            return result;
        }

        @Override
        public DeferredBlock getDeferred(@Nullable T discriminator) {
            return getRegistration(discriminator).asDeferredBlock();
        }

        @Override
        public Block get(@Nullable T discriminator) {
            return getRegistration(discriminator).asHolder().value();
        }

        @Override
        public void forEach(BiConsumer<T, Block> consumer) {
            map.forEach((discriminator, registration) -> consumer.accept(discriminator, registration.asHolder().value()));
        }

        @Override
        public void forEachDeferred(BiConsumer<T, DeferredBlock> consumer) {
            map.forEach((discriminator, registration) -> consumer.accept(discriminator, registration.asDeferredBlock()));
        }

        @Override
        public void forEach(Consumer<Block> consumer) {
            map.values().forEach(registration -> consumer.accept(registration.asHolder().value()));
        }

        @Override
        public void forEachDeferred(Consumer<DeferredBlock> consumer) {
            map.values().forEach(registration -> consumer.accept(registration.asDeferredBlock()));
        }

        @Override
        public void forEachDiscriminated(BiConsumer<T, Block> consumer) {
            map.forEach((discriminator, registration) -> {
                if (discriminator != null) {
                    consumer.accept(discriminator, registration.asHolder().value());
                }
            });
        }

        @Override
        public void forEachDiscriminatedDeferred(BiConsumer<T, DeferredBlock> consumer) {
            map.forEach((discriminator, registration) -> {
                if (discriminator != null) {
                    consumer.accept(discriminator, registration.asDeferredBlock());
                }
            });
        }

        @Override
        public Collection<DeferredBlock> getAllDeferred() {
            return map.values().stream().map(BalmBlockRegistration::asDeferredBlock).toList();
        }

        @Override
        public Collection<Block> getAll() {
            return map.values().stream().map(BalmBlockRegistration::asHolder).map(Holder::value).toList();
        }

        @Override
        public Collection<DeferredBlock> getDiscriminatedDeferred() {
            return map.entrySet().stream()
                    .filter(it -> it.getKey() != null)
                    .map(it -> it.getValue().asDeferredBlock())
                    .toList();
        }

        @Override
        public Collection<Block> getDiscriminated() {
            return map.entrySet().stream()
                    .filter(it -> it.getKey() != null)
                    .map(it -> it.getValue().asHolder().value())
                    .toList();
        }
    }

    private static class BalmDiscriminatedBlockRegistrationImpl<T> implements BalmDiscriminatedBlockRegistration<T> {
        private final Map<T, BalmBlockRegistration> map;
        private final BalmBlockFactory factory;
        private final Function<T, String> nameFunction;
        private final BiFunction<T, BlockBehaviour.Properties, Block> constructor;
        private final BiFunction<T, BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesFunction;

        private BalmDiscriminatedBlockRegistrationImpl(
                Map<T, BalmBlockRegistration> map,
                BalmBlockFactory factory,
                Function<T, String> nameFunction,
                BiFunction<T, BlockBehaviour.Properties, Block> constructor,
                BiFunction<T, BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesFunction
        ) {
            this.map = map;
            this.factory = factory;
            this.nameFunction = nameFunction;
            this.constructor = constructor;
            this.propertiesFunction = propertiesFunction;
        }

        @Override
        public BalmDiscriminatedBlockRegistration<T> forEach(BiConsumer<T, BalmBlockRegistration> consumer) {
            map.forEach(consumer);
            return this;
        }

        @Override
        public BalmDiscriminatedBlockRegistration<T> withNullDiscriminator() {
            final var name = nameFunction.apply(null);
            final var registration = factory.register(name, properties -> constructor.apply(null, properties), properties -> propertiesFunction.apply(null, properties));
            map.put(null, registration);
            return this;
        }

        @Override
        public DiscriminatedBlocks<T> asDiscriminatedBlocks() {
            return new DiscriminatedBlocksImpl<>(map);
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
            final var itemResourceKey = ResourceKey.create(Registries.ITEM, blockResourceKey.location());
            registrar.register(itemResourceKey, (Supplier<Item>) () -> constructor.apply(holder.value(), properties.get()));
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
