package net.blay09.mods.balm.world.item;

import net.blay09.mods.balm.core.BalmRegistrar;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public class BalmItemFactoryImpl implements BalmItemFactory {

    private final BalmRegistrar registrar;
    private final String namespace;

    public BalmItemFactoryImpl(BalmRegistrar registrar, String namespace) {
        this.registrar = registrar;
        this.namespace = namespace;
    }

    @Override
    public BalmItemRegistration register(String name, Function<Item.Properties, Item> constructor, Supplier<Item.Properties> properties) {
        final var resourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, name);
        final var resourceKey = ResourceKey.create(Registries.ITEM, resourceLocation);
        final var holder = registrar.register(resourceKey, (id) -> constructor.apply(properties.get().setId(resourceKey)));
        return new BalmItemRegistrationImpl(holder);
    }

    private static class BalmItemRegistrationImpl implements BalmItemRegistration {
        private final Holder<Item> holder;
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
