package net.blay09.mods.balm.api.container;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SubContainer implements Container, WorldlyContainer {
    private final Container container;
    private final int minSlot;
    private final int maxSlot;

    public SubContainer(Container container, int minSlot, int maxSlot) {
        this.container = container;
        this.minSlot = minSlot;
        this.maxSlot = maxSlot;
    }

    @Override
    public int getContainerSize() {
        return maxSlot - minSlot;
    }

    @Override
    public ItemStack getItem(int slot) {
        return containsSlot(slot) ? container.getItem(slot + minSlot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return containsSlot(slot) ? container.removeItem(slot + minSlot, amount) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return containsSlot(slot) ? container.removeItemNoUpdate(slot + minSlot) : ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        if (containsSlot(slot)) {
            container.setItem(slot + minSlot, itemStack);
        }
    }

    @Override
    public void startOpen(ContainerUser user) {
        container.startOpen(user);
    }

    @Override
    public void stopOpen(ContainerUser user) {
        container.stopOpen(user);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return containsSlot(slot) && container.canPlaceItem(slot + minSlot, itemStack);
    }

    @Override
    public boolean isEmpty() {
        for (int i = minSlot; i < maxSlot; i++) {
            if (!container.getItem(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public int getMaxStackSize() {
        return container.getMaxStackSize();
    }

    @Override
    public void setChanged() {
        container.setChanged();
    }

    private boolean containsSlot(int slot) {
        return slot + minSlot < maxSlot;
    }

    public boolean containsOuterSlot(int slot) {
        return slot >= minSlot && slot < maxSlot;
    }

    @Override
    public void clearContent() {
        for (int i = minSlot; i < maxSlot; i++) {
            container.setItem(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canTakeItem(Container container, int slot, ItemStack itemStack) {
        return containsSlot(slot) && this.container.canTakeItem(this.container, slot + minSlot, itemStack);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (container instanceof WorldlyContainer worldContainer) {
            final var original = worldContainer.getSlotsForFace(direction);
            final var result = new ArrayList<Integer>();
            for (int outerSlot : original) {
                if (containsOuterSlot(outerSlot)) {
                    result.add(outerSlot - minSlot);
                }
            }
            return result.stream().mapToInt(i -> i).toArray();
        } else {
            final var result = new int[getContainerSize()];
            for (int i = 0; i < result.length; i++) {
                result[i] = i;
            }
            return result;
        }
    }

    public int[] getOuterSlotsForFace(Direction direction) {
        if (container instanceof WorldlyContainer worldContainer) {
            final var original = worldContainer.getSlotsForFace(direction);
            final var result = new ArrayList<Integer>();
            for (int outerSlot : original) {
                if (containsOuterSlot(outerSlot)) {
                    result.add(outerSlot);
                }
            }
            return result.stream().mapToInt(i -> i).toArray();
        } else {
            final var slots = new int[maxSlot - minSlot];
            for (int i = 0; i < slots.length; i++) {
                slots[i] = i + minSlot;
            }
            return slots;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        if (container instanceof WorldlyContainer worldlyContainer) {
            return containsSlot(slot) && worldlyContainer.canPlaceItemThroughFace(slot + minSlot, itemStack, direction);
        } else {
            return canPlaceItem(slot, itemStack);
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        if (container instanceof WorldlyContainer worldlyContainer) {
            return containsSlot(slot) && worldlyContainer.canTakeItemThroughFace(slot + minSlot, itemStack, direction);
        } else {
            return canTakeItem(this, slot, itemStack);
        }
    }
}
