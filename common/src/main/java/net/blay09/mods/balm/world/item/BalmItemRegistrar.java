package net.blay09.mods.balm.world.item;

import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Provides convenience access to registering items.
 */
public interface BalmItemRegistrar {

    default BalmItemRegistration register(String name, Function<Item.Properties, Item> constructor) {
        return register(name, constructor, Item.Properties::new);
    }

    default BalmItemRegistration register(String name, Function<Item.Properties, Item> constructor, Function<Item.Properties, Item.Properties> propertiesBuilder) {
        return register(name, constructor, () -> propertiesBuilder.apply(new Item.Properties()));
    }

    default BalmItemRegistration register(String name, Function<Item.Properties, Item> constructor, Item.Properties properties) {
        return register(name, constructor, () -> properties);
    }

    BalmItemRegistration register(String name, Function<Item.Properties, Item> constructor, Supplier<Item.Properties> propertiesSupplier);

    default <T> BalmDiscriminatedItemRegistration<T> registerDiscriminated(T[] values, Function<T, String> nameFunction, BiFunction<T, Item.Properties, Item> constructor, Function<Item.Properties, Item.Properties> propertiesSupplier) {
        return registerDiscriminated(Set.of(values), nameFunction, constructor, propertiesSupplier);
    }

    default <T> BalmDiscriminatedItemRegistration<T> registerDiscriminated(T[] values, Function<T, String> nameFunction, BiFunction<T, Item.Properties, Item> constructor, BiFunction<T, Item.Properties, Item.Properties> propertiesSupplier) {
        return registerDiscriminated(Set.of(values), nameFunction, constructor, propertiesSupplier);
    }

    default <T> BalmDiscriminatedItemRegistration<T> registerDiscriminated(Set<@Nullable T> values, Function<T, String> nameFunction, BiFunction<T, Item.Properties, Item> constructor, Function<Item.Properties, Item.Properties> propertiesSupplier) {
        return registerDiscriminated(values, nameFunction, constructor, (discriminator, properties) -> propertiesSupplier.apply(properties));
    }

    <T> BalmDiscriminatedItemRegistration<T> registerDiscriminated(Set<@Nullable T> values, Function<T, String> nameFunction, BiFunction<T, Item.Properties, Item> constructor, BiFunction<T, Item.Properties, Item.Properties> propertiesSupplier);

}
