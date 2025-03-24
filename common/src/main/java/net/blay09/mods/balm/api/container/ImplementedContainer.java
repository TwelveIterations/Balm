package net.blay09.mods.balm.api.container;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ImplementedContainer extends Container {
    /**
     * Creates an inventory from the item list.
     */
    static ImplementedContainer of(NonNullList<ItemStack> items) {
        return () -> items;
    }

    /**
     * Creates a new inventory with the specified size.
     */
    static ImplementedContainer ofSize(int size) {
        return of(NonNullList.withSize(size, ItemStack.EMPTY));
    }

    static NonNullList<ItemStack> deserializeInventory(CompoundTag tag, int minimumSize, HolderLookup.Provider provider) {
        int size = Math.max(minimumSize, tag.getIntOr("Size", minimumSize));
        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        tag.getList("Items").ifPresent(itemTags -> {
            for (int i = 0; i < itemTags.size(); i++) {
                itemTags.getCompound(i).ifPresent(itemTag -> {
                    int slot = itemTag.getIntOr("Slot", -1);
                    if (slot >= 0 && slot < items.size()) {
                        items.set(slot, ItemStack.parse(provider, itemTag).orElse(ItemStack.EMPTY));
                    }
                });
            }
        });
        return items;
    }

    /**
     * Retrieves the item list of this inventory.
     * Must return the same instance every time it's called.
     */
    NonNullList<ItemStack> getItems();

    /**
     * Returns the inventory size.
     */
    @Override
    default int getContainerSize() {
        return getItems().size();
    }

    /**
     * Checks if the inventory is empty.
     *
     * @return true if this inventory has only empty stacks, false otherwise.
     */
    @Override
    default boolean isEmpty() {
        for (int i = 0; i < getContainerSize(); i++) {
            ItemStack stack = getItem(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves the item in the slot.
     */
    @Override
    default ItemStack getItem(int slot) {
        return getItems().get(slot);
    }

    /**
     * Removes items from an inventory slot.
     *
     * @param slot  The slot to remove from.
     * @param count How many items to remove. If there are less items in the slot than what are requested,
     *              takes all items in that slot.
     */
    @Override
    default ItemStack removeItem(int slot, int count) {
        ItemStack result = ContainerHelper.removeItem(getItems(), slot, count);
        if (!result.isEmpty()) {
            setChanged();
        }
        slotChanged(slot);
        return result;
    }

    /**
     * Removes all items from an inventory slot.
     *
     * @param slot The slot to remove from.
     */
    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        ItemStack itemStack = ContainerHelper.takeItem(getItems(), slot);
        slotChanged(slot);
        return itemStack;
    }

    /**
     * Replaces the current stack in an inventory slot with the provided stack.
     *
     * @param slot  The inventory slot of which to replace the itemstack.
     * @param stack The replacing itemstack. If the stack is too big for
     *              this inventory ({@link Container#getMaxStackSize()}),
     *              it gets resized to this inventory's maximum amount.
     */
    @Override
    default void setItem(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
        slotChanged(slot);
    }

    /**
     * Clears the inventory.
     */
    @Override
    default void clearContent() {
        getItems().clear();
        for (int i = 0; i < getItems().size(); i++) {
            slotChanged(i);
        }
    }

    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     */
    @Override
    default void setChanged() {
        // Override if you want behavior.
    }

    default void slotChanged(int slot) {
        // Override if you want behavior.
    }

    /**
     * @return true if the player can use the inventory, false otherwise.
     */
    @Override
    default boolean stillValid(Player player) {
        return true;
    }

    default CompoundTag serializeInventory(HolderLookup.Provider provider) {
        NonNullList<ItemStack> items = getItems();
        ListTag itemTags = new ListTag();
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putInt("Slot", i);
                itemTags.add(items.get(i).save(provider, itemTag));
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("Items", itemTags);
        nbt.putInt("Size", items.size());
        return nbt;
    }
}
