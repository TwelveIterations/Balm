package net.blay09.mods.balm.world.level.block.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.level.block.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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

    private boolean useBlockDescriptionPrefixForItems;

    public BalmBlockRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public void enableBlockDescriptionPrefixForItems() {
        useBlockDescriptionPrefixForItems = true;
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
        final var holder = registrar.register(resourceKey, (_) -> constructor.apply(properties.get().setId(resourceKey)));
        return new BalmBlockRegistrationImpl(namespace, registrar, holder, useBlockDescriptionPrefixForItems);
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
        public Stream<Entry<T, DeferredBlock>> sortedEntries(Comparator<T> comparator) {
            return entrySet().stream().sorted(Entry.comparingByKey(comparator));
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
        private final String namespace;
        private final BalmRegistrar registrar;
        private final Holder<Block> holder;
        private final boolean useBlockDescriptionPrefixForItems;
        @Nullable
        private DeferredBlock deferredBlock;

        private BalmBlockRegistrationImpl(String namespace, BalmRegistrar registrar, Holder<Block> holder, boolean useBlockDescriptionPrefixForItems) {
            this.namespace = namespace;
            this.registrar = registrar;
            this.holder = holder;
            this.useBlockDescriptionPrefixForItems = useBlockDescriptionPrefixForItems;
        }

        @Override
        public BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor, Function<Item.Properties, Item.Properties> propertiesBuilder) {
            final var blockResourceKey = holder.unwrapKey().orElseThrow();
            return withItem(blockResourceKey.identifier().getPath(), constructor, propertiesBuilder);
        }

        @Override
        public BalmBlockRegistration withItem(String name, BiFunction<Block, Item.Properties, BlockItem> constructor, Function<Item.Properties, Item.Properties> propertiesBuilder) {
            final var itemIdentifier = Identifier.fromNamespaceAndPath(namespace, name);
            final var itemResourceKey = ResourceKey.create(Registries.ITEM, itemIdentifier);
            registrar.register(itemResourceKey, (_) -> constructor.apply(holder.value(), propertiesBuilder.apply(patchItemProperties(new Item.Properties().setId(itemResourceKey)))));
            return this;
        }

        @Override
        @Deprecated
        public BalmBlockRegistration withItem(BiFunction<Block, Item.Properties, BlockItem> constructor, Supplier<Item.Properties> properties) {
            final var blockResourceKey = holder.unwrapKey().orElseThrow();
            final var itemResourceKey = ResourceKey.create(Registries.ITEM, blockResourceKey.identifier());
            registrar.register(itemResourceKey, (_) -> constructor.apply(holder.value(), patchItemProperties(properties.get().setId(itemResourceKey))));
            return this;
        }

        private Item.Properties patchItemProperties(Item.Properties properties) {
            if (useBlockDescriptionPrefixForItems) {
                return properties.useBlockDescriptionPrefix();
            }
            return properties;
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
