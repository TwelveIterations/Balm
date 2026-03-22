package net.blay09.mods.balm.world.item.internal;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.blay09.mods.balm.world.item.BalmDiscriminatedItemRegistration;
import net.blay09.mods.balm.world.item.BalmItemRegistrar;
import net.blay09.mods.balm.world.item.BalmItemRegistration;
import net.blay09.mods.balm.world.item.DiscriminatedItems;
import net.blay09.mods.balm.world.item.DeferredItem;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BalmItemRegistrarImpl implements BalmItemRegistrar {

    private final BalmRegistrar registrar;
    private final String namespace;

    public BalmItemRegistrarImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public void addAlias(Identifier oldId, Identifier newId) {
        registrar.addAlias(
                Registries.ITEM,
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
    public BalmItemRegistration register(String name, Function<Item.Properties, Item> constructor, Supplier<Item.Properties> properties) {
        final var identifier = Identifier.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.ITEM, identifier);
        final var holder = registrar.register(resourceKey, (id) -> constructor.apply(properties.get().setId(resourceKey)));
        return new BalmItemRegistrationImpl(holder);
    }

    @Override
    public <T> BalmDiscriminatedItemRegistration<T> registerDiscriminated(Set<@Nullable T> values, Function<T, String> nameFunction, BiFunction<T, Item.Properties, Item> constructor, BiFunction<T, Item.Properties, Item.Properties> propertiesFunction) {
        final var map = new BalmDiscriminatedItemRegistrationImpl<T>();
        for (final var value : values) {
            final var name = nameFunction.apply(value);
            final var registration = register(name, (properties) -> constructor.apply(value, properties), () -> propertiesFunction.apply(value, new Item.Properties()));
            map.put(value, registration);
        }
        return map;
    }

    private static class DiscriminatedItemsImpl<T> extends HashMap<@Nullable T, DeferredItem> implements DiscriminatedItems<T> {

        @Override
        public Stream<Entry<@Nullable T, DeferredItem>> sortedEntries(Comparator<T> comparator) {
            return entrySet().stream().sorted(Entry.comparingByKey(comparator));
        }

    }

    private static class BalmDiscriminatedItemRegistrationImpl<T> extends HashMap<@Nullable T, BalmItemRegistration> implements BalmDiscriminatedItemRegistration<T> {
        @Override
        public DiscriminatedItems<T> asDiscriminatedItems() {
            final var map = new DiscriminatedItemsImpl<T>();
            forEach((key, registration) -> map.put(key, registration.asDeferredItem()));
            return map;
        }
    }

    private static class BalmItemRegistrationImpl implements BalmItemRegistration {
        private final Holder<Item> holder;
        @Nullable
        private DeferredItem deferredItem;

        private BalmItemRegistrationImpl(Holder<Item> holder) {
            this.holder = holder;
        }

        @Override
        public Holder<Item> asHolder() {
            return holder;
        }

        @Override
        public DeferredItem asDeferredItem() {
            if (deferredItem == null) {
                deferredItem = new DeferredItemImpl(holder);
            }
            return deferredItem;
        }
    }
}
