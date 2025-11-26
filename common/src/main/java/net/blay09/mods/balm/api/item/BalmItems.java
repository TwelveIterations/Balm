package net.blay09.mods.balm.api.item;

import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.registries.Registries;
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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(Consumer)} and {@link net.blay09.mods.balm.core.BalmRegistrars#creativeModeTabs(Consumer)} instead.
 */
@Deprecated
public interface BalmItems {

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(Consumer)} instead.
     */
    @Deprecated
    static Item.Properties itemProperties(ResourceLocation identifier) {
        return new Item.Properties();
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(Consumer)} instead.
     */
    @Deprecated
    static ResourceKey<Item> itemId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.ITEM, identifier);
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(Consumer)} instead.
     */
    @Deprecated
    static BlockItem blockItem(Block block, ResourceLocation identifier) {
        return new BlockItem(block, itemProperties(identifier));
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(Consumer)} instead.
     */
    @Deprecated
    default DeferredObject<Item> registerItem(Function<ResourceLocation, Item> supplier, ResourceLocation identifier) {
        return registerItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#items(Consumer)} instead.
     */
    @Deprecated
    DeferredObject<Item> registerItem(Function<ResourceLocation, Item> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#creativeModeTabs(Consumer)} instead.
     */
    @Deprecated
    DeferredObject<CreativeModeTab> registerCreativeModeTab(Supplier<ItemStack> iconSupplier, ResourceLocation identifier);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#creativeModeTabs(Consumer)} instead.
     */
    @Deprecated
    void addToCreativeModeTab(ResourceLocation tabIdentifier, Supplier<ItemLike[]> itemsSupplier);

    /**
     * @deprecated Use {@link net.blay09.mods.balm.core.BalmRegistrars#creativeModeTabs(Consumer)} instead.
     */
    @Deprecated
    void setCreativeModeTabSorting(ResourceLocation tabIdentifier, Comparator<ItemLike> comparator);

    /**
     * @deprecated Use {@link #itemProperties(ResourceLocation)} instead
     */
    @Deprecated
    default Item.Properties itemProperties() {
        return new Item.Properties();
    }

    /**
     * @deprecated Use {@link #registerItem(Function, ResourceLocation)} instead
     */
    @Deprecated
    default DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier) {
        return registerItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * @deprecated Use {@link #registerItem(Function, ResourceLocation, ResourceLocation)} instead
     */
    @Deprecated
    default DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        return registerItem((id) -> supplier.get(), identifier, creativeTab);
    }

    @Deprecated
    BalmItems scoped(String modId);
}
