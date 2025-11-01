package net.blay09.mods.balm.world.item;

import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides convenience access to registering items.
 */
public interface BalmItemFactory {

    default BalmItemRegistration register(String location, Function<Item.Properties, Item> constructor, Function<Item.Properties, Item.Properties> propertiesBuilder) {
        return register(location, constructor, () -> propertiesBuilder.apply(new Item.Properties()));
    }

    default BalmItemRegistration register(String location, Function<Item.Properties, Item> constructor, Item.Properties properties) {
        return register(location, constructor, () -> properties);
    }

    BalmItemRegistration register(String location, Function<Item.Properties, Item> constructor, Supplier<Item.Properties> propertiesSupplier);

}
