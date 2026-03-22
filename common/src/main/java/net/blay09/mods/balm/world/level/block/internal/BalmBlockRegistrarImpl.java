package net.blay09.mods.balm.world.level.block.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.level.block.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
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
    public void addAlias(Identifier oldId, Identifier newId) {
        registrar.addAlias(
                Registries.BLOCK,
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
    public BalmBlockRegistration register(String name, Function<BlockBehaviour.Properties, Block> constructor, Supplier<BlockBehaviour.Properties> properties) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.BLOCK, identifier);
        final var holder = registrar.register(resourceKey, (id) -> constructor.apply(properties.get().setId(resourceKey)));
        return new BalmBlockRegistrationImpl(registrar, holder);
    }

    @Override
    public <T> BalmDiscriminatedBlockRegistration<T> registerDiscriminated(Set<@Nullable T> values, Function<@Nullable T, String> nameFunction, BiFunction<@Nullable T, BlockBehaviour.Properties, Block> constructor, BiFunction<@Nullable T, BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesFunction) {
        final var map = new BalmDiscriminatedBlockRegistrationImpl<T>();
        for (final var value : values) {
            final var name = nameFunction.apply(value);
            final var registration = register(name, (properties) -> constructor.apply(value, properties), properties -> propertiesFunction.apply(value, properties));
            map.put(value, registration);
        }
        return map;
    }

    private static class DiscriminatedBlocksImpl<T> extends HashMap<@Nullable T, DeferredBlock> implements DiscriminatedBlocks<T> {
        @Override
        public Stream<Entry<T, DeferredBlock>> sortedEntries(Comparator<T> comparator) {
            return entrySet().stream().sorted(Entry.comparingByKey(comparator));
        }
    }

    private static class BalmDiscriminatedBlockRegistrationImpl<T> extends HashMap<@Nullable T, BalmBlockRegistration> implements BalmDiscriminatedBlockRegistration<T> {

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
        @Nullable
        private DeferredBlock deferredBlock;

        private BalmBlockRegistrationImpl(BalmRegistrar registrar, Holder<Block> holder) {
            this.registrar = registrar;
            this.holder = holder;
        }

        @Override
        public BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor, Supplier<Item.Properties> properties) {
            final var blockResourceKey = holder.unwrapKey().orElseThrow();
            final var itemResourceKey = ResourceKey.create(Registries.ITEM, blockResourceKey.identifier());
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
