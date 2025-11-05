package net.blay09.mods.balm.world.level.block.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.level.block.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashMap;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BalmBlockRegistrarImpl implements BalmBlockRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    public BalmBlockRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public BalmBlockRegistration register(String name, Function<BlockBehaviour.Properties, Block> constructor, Supplier<BlockBehaviour.Properties> properties) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.BLOCK, resourceLocation);
        final var holder = registrar.register(resourceKey, (id) -> constructor.apply(properties.get().setId(resourceKey)));
        return new BalmBlockRegistrationImpl(registrar, holder);
    }

    @Override
    public <T> BalmDiscriminatedBlockRegistration<T> registerDiscriminated(Set<T> values, Function<T, String> nameFunction, BiFunction<T, BlockBehaviour.Properties, Block> constructor, BiFunction<T, BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesFunction) {
        final var map = new BalmDiscriminatedBlockRegistrationImpl<T>();
        for (final var value : values) {
            final var name = nameFunction.apply(value);
            final var registration = register(name, (properties) -> constructor.apply(value, properties), properties -> propertiesFunction.apply(value, properties));
            map.put(value, registration);
        }
        return map;
    }

    private static class DiscriminatedBlocksImpl<T> extends HashMap<T, DeferredBlock> implements DiscriminatedBlocks<T> {
        @Override
        public Stream<Entry<T, DeferredBlock>> filterNonNullDiscriminatorEntries() {
            return entrySet().stream().filter(it -> it.getKey() != null);
        }

        @Override
        public Stream<DeferredBlock> filterNonNullDiscriminators() {
            return entrySet().stream().filter(it -> it.getKey() != null).map(Entry::getValue);
        }
    }

    private static class BalmDiscriminatedBlockRegistrationImpl<T> extends HashMap<T, BalmBlockRegistration> implements BalmDiscriminatedBlockRegistration<T> {

        @Override
        public DiscriminatedBlocks<T> asDiscriminatedBlocks() {
            final var map = new DiscriminatedBlocksImpl<T>();
            forEach((key, registration) -> map.put(key, registration.asDeferredBlock()));
            return map;
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
            registrar.register(itemResourceKey, (id) -> constructor.apply(holder.value(), properties.get().setId(itemResourceKey)));
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
