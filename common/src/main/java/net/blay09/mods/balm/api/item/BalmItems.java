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
import net.minecraft.resources.Identifier;
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
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(String, Consumer)} and {@link net.blay09.mods.balm.core.BalmRegistrars#creativeModeTabs(String, Consumer)} instead.
 */
@Deprecated
public interface BalmItems {

    Multimap<String, ResourceKey<Item>> legacyCreativeModeTabItems = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
    Map<String, Comparator<ItemLike>> legacyCreativeModeTabSorting = new ConcurrentHashMap<>();

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(String, Consumer)} instead.
     */
    @Deprecated
    static Item.Properties itemProperties(Identifier identifier) {
        return new Item.Properties().setId(itemId(identifier));
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(String, Consumer)} instead.
     */
    @Deprecated
    static ResourceKey<Item> itemId(Identifier identifier) {
        return ResourceKey.create(Registries.ITEM, identifier);
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(String, Consumer)} instead.
     */
    @Deprecated
    static BlockItem blockItem(Block block, Identifier identifier) {
        return new BlockItem(block, itemProperties(identifier));
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(String, Consumer)} instead.
     */
    @Deprecated
    default DeferredObject<Item> registerItem(Function<Identifier, Item> supplier, Identifier identifier) {
        return registerItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(String, Consumer)} instead.
     */
    @Deprecated
    default DeferredObject<Item> registerItem(Function<Identifier, Item> constructor, Identifier identifier, @Nullable Identifier creativeTab) {
        final var resourceKey = ResourceKey.create(Registries.ITEM, identifier);
        final var holder = Balm.getRuntime().registrar().register(resourceKey, constructor);
        BalmItems.legacyCreativeModeTabItems.put(identifier.getNamespace(), resourceKey);
        return new DeferredObject<>(identifier, holder::value, holder::isBound);
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#creativeModeTabs(String, Consumer)} instead.
     */
    @Deprecated
    default DeferredObject<CreativeModeTab> registerCreativeModeTab(Supplier<ItemStack> iconSupplier, Identifier identifier) {
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
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#creativeModeTabs(String, Consumer)} instead.
     */
    default void addToCreativeModeTab(Identifier tabIdentifier, Supplier<ItemLike[]> itemsSupplier) {
        Balm.getEvents().onEvent(BuildCreativeModeTabContentsEvent.class, event -> {
            final var identifier = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(event.getTab());
            if (tabIdentifier.equals(identifier)) {
                final var items = itemsSupplier.get();
                for (final var item : items) {
                    event.getOutput().accept(item);
                }
            }
        });
    }

    /**
     * Use {@link net.blay09.mods.balm.core.BalmRegistrars#creativeModeTabs(String, Consumer)} instead.
     */
    default void setCreativeModeTabSorting(Identifier tabIdentifier, Comparator<ItemLike> comparator) {
        legacyCreativeModeTabSorting.put(tabIdentifier.getNamespace(), comparator);
    }

    default BalmItems scoped(String modId) {
        return this;
    }

    BalmItems LEGACY = new BalmItems() {
    };
}
