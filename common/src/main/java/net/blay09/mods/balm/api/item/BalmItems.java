package net.blay09.mods.balm.api.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.world.item.BuildCreativeModeTabContentsEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Use {@link Balm#items(String, Consumer)} and {@link Balm#creativeModeTabs(String, Consumer)} instead.
 */
public interface BalmItems {

    Multimap<String, ResourceKey<Item>> legacyCreativeModeTabItems = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
    Map<String, Comparator<ItemLike>> legacyCreativeModeTabSorting = new ConcurrentHashMap<>();

    /**
     * Use {@link Balm#items(String, Consumer)} instead.
     */
    @Deprecated
    static Item.Properties itemProperties(ResourceLocation identifier) {
        return new Item.Properties().setId(itemId(identifier));
    }

    /**
     * Use {@link Balm#items(String, Consumer)} instead.
     */
    @Deprecated
    static ResourceKey<Item> itemId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.ITEM, identifier);
    }

    /**
     * Use {@link Balm#items(String, Consumer)} instead.
     */
    @Deprecated
    static BlockItem blockItem(Block block, ResourceLocation identifier) {
        return new BlockItem(block, itemProperties(identifier));
    }

    /**
     * Use {@link Balm#items(String, Consumer)} instead.
     */
    @Deprecated
    default DeferredObject<Item> registerItem(Function<ResourceLocation, Item> supplier, ResourceLocation identifier) {
        return registerItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * Use {@link Balm#items(String, Consumer)} instead.
     */
    @Deprecated
    default DeferredObject<Item> registerItem(Function<ResourceLocation, Item> constructor, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        final var resourceKey = ResourceKey.create(Registries.ITEM, identifier);
        final var holder = Balm.registrar().register(resourceKey, constructor);
        BalmItems.legacyCreativeModeTabItems.put(identifier.getNamespace(), resourceKey);
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * Use {@link Balm#creativeModeTabs(String, Consumer)} instead.
     */
    @Deprecated
    default DeferredObject<CreativeModeTab> registerCreativeModeTab(Supplier<ItemStack> iconSupplier, ResourceLocation identifier) {
        final var holder = Balm.getRuntime().creativeModeTabs(identifier.getNamespace()).register(identifier.getPath(), builder -> {
            final var displayName = Component.translatable(identifier.toLanguageKey("itemGroup"));
            return builder
                    .title(displayName)
                    .icon(iconSupplier)
                    .displayItems((enabledFeatures, entries) -> {
                        final var comparator = legacyCreativeModeTabSorting.get(identifier.getNamespace());
                        final var items = legacyCreativeModeTabItems.get(identifier.getNamespace())
                                .stream()
                                .map(BuiltInRegistries.ITEM::getValue)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        if (comparator != null) {
                            items.sort(comparator);
                        }
                        items.forEach(entries::accept);
                    });
        }).asHolder();
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * Use {@link Balm#creativeModeTabs(String, Consumer)} instead.
     */
    default void addToCreativeModeTab(ResourceLocation tabIdentifier, Supplier<ItemLike[]> itemsSupplier) {
        Balm.getEvents().onEvent(BuildCreativeModeTabContentsEvent.class, event -> {
            final var items = itemsSupplier.get();
            for (final var item : items) {
                event.getOutput().accept(item);
            }
        });
    }

    /**
     * Use {@link Balm#creativeModeTabs(String, Consumer)} instead.
     */
    default void setCreativeModeTabSorting(ResourceLocation tabIdentifier, Comparator<ItemLike> comparator) {
        legacyCreativeModeTabSorting.put(tabIdentifier.getNamespace(), comparator);
    }

    default BalmItems scoped(String modId) {
        return this;
    }

    BalmItems LEGACY = new BalmItems() {
    };
}
