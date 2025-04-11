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
import java.util.function.Function;
import java.util.function.Supplier;

public interface BalmItems {

    static Item.Properties itemProperties(ResourceLocation identifier) {
        return new Item.Properties();
    }

    static ResourceKey<Item> itemId(ResourceLocation identifier) {
        return ResourceKey.create(Registries.ITEM, identifier);
    }

    static BlockItem blockItem(Block block, ResourceLocation identifier) {
        return new BlockItem(block, itemProperties(identifier));
    }

    default DeferredObject<Item> registerItem(Function<ResourceLocation, Item> supplier, ResourceLocation identifier) {
        return registerItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    DeferredObject<Item> registerItem(Function<ResourceLocation, Item> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab);

    DeferredObject<CreativeModeTab> registerCreativeModeTab(Supplier<ItemStack> iconSupplier, ResourceLocation identifier);

    void addToCreativeModeTab(ResourceLocation tabIdentifier, Supplier<ItemLike[]> itemsSupplier);

    void setCreativeModeTabSorting(ResourceLocation tabIdentifier, Comparator<ItemLike> comparator);

    /**
     * @deprecated Use {@link #itemProperties(ResourceLocation)} instead
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default Item.Properties itemProperties() {
        return new Item.Properties();
    }

    /**
     * @deprecated Use {@link #registerItem(Function, ResourceLocation)} instead
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier) {
        return registerItem(supplier, identifier, identifier.withPath(identifier.getNamespace()));
    }

    /**
     * @deprecated Use {@link #registerItem(Function, ResourceLocation, ResourceLocation)} instead
     */
    @Deprecated(forRemoval = true, since = "1.21.5")
    default DeferredObject<Item> registerItem(Supplier<Item> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        return registerItem((id) -> supplier.get(), identifier, creativeTab);
    }

    BalmItems scoped(String modId);
}
