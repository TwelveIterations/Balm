package net.blay09.mods.balm.api.container;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ContainerUtils {
    public static ItemStack extractItem(Container container, int slot, int amount, boolean simulate) {
        if (container instanceof ExtractionAwareContainer extractionAwareContainer && !extractionAwareContainer.canExtractItem(slot)) {
            return ItemStack.EMPTY;
        }

        if (amount == 0) {
            return ItemStack.EMPTY;
        }

        if (slot < 0 || slot >= container.getContainerSize()) {
            return ItemStack.EMPTY;
        }

        ItemStack existing = container.getItem(slot);
        if (existing.isEmpty()) {
            return ItemStack.EMPTY;
        }

        int toExtract = Math.min(amount, existing.getMaxStackSize());
        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                container.setItem(slot, ItemStack.EMPTY);
                return existing;
            }

            return existing.copy();
        } else {
            if (!simulate) {
                container.setItem(slot, copyStackWithSize(existing, existing.getCount() - toExtract));
            }

            return copyStackWithSize(existing, toExtract);
        }
    }

    public static ItemStack insertItem(Container container, ItemStack itemStack, boolean simulate) {
        if (container == null || itemStack.isEmpty()) {
            return itemStack;
        }

        for (int i = 0; i < container.getContainerSize(); i++) {
            itemStack = insertItem(container, i, itemStack, simulate);
            if (itemStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        return itemStack;
    }

    public static ItemStack insertItem(Container container, int slot, ItemStack itemStack, boolean simulate) {
        if (container == null || itemStack.isEmpty()) {
            return itemStack;
        }

        if (slot < 0 || slot >= container.getContainerSize()) {
            return ItemStack.EMPTY;
        }

        ItemStack existing = container.getItem(slot);

        int limit = Math.min(container.getMaxStackSize(), itemStack.getMaxStackSize());
        if (!existing.isEmpty()) {
            if (!Balm.getHooks().canItemsStack(itemStack, existing)) {
                return itemStack;
            }

            limit -= existing.getCount();
        }

        if (limit <= 0) {
            return itemStack;
        }

        boolean reachedLimit = itemStack.getCount() > limit;
        if (!simulate) {
            if (existing.isEmpty()) {
                container.setItem(slot, reachedLimit ? copyStackWithSize(itemStack, limit) : itemStack);
            } else {
                existing.grow(reachedLimit ? limit : itemStack.getCount());
                container.setChanged();
            }
        }

        return reachedLimit ? copyStackWithSize(itemStack, itemStack.getCount() - limit) : ItemStack.EMPTY;
    }

    public static ItemStack insertItemStacked(Container container, ItemStack itemStack, boolean simulate) {
        if (container == null || itemStack.isEmpty()) {
            return itemStack;
        }

        if (!itemStack.isStackable()) {
            return insertItem(container, itemStack, simulate);
        }

        int firstEmptySlot = -1;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack slotStack = container.getItem(i);
            if (slotStack.isEmpty() && firstEmptySlot == -1) {
                firstEmptySlot = i;
                continue;
            }

            if (slotStack.isStackable() && ItemStack.isSameItemSameTags(slotStack, itemStack)) {
                itemStack = insertItem(container, i, itemStack, simulate);
            }

            if (itemStack.isEmpty()) {
                return ItemStack.EMPTY;
            }
        }

        if (firstEmptySlot != -1) {
            for (int i = firstEmptySlot; i < container.getContainerSize(); i++) {
                if (container.getItem(i).isEmpty()) {
                    itemStack = insertItem(container, i, itemStack, simulate);

                    if (itemStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }

        return itemStack;
    }

    public static void dropItems(Container container, Level level, BlockPos pos) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (!itemStack.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, itemStack);
                itemEntity.setDeltaMovement(0f, 0.2f, 0f);
                level.addFreshEntity(itemEntity);
            }
        }
        container.clearContent();
    }

    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        if (size == 0) {
            return ItemStack.EMPTY;
        }

        ItemStack copy = itemStack.copy();
        copy.setCount(size);
        return copy;
    }
}
