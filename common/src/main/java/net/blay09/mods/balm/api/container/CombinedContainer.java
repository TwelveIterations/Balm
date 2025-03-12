package net.blay09.mods.balm.api.container;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CombinedContainer implements Container, WorldlyContainer {
    private final Container[] containers;
    private final int[] baseIndex;
    private final int totalSlots;

    public CombinedContainer(Container... containers) {
        this.containers = containers;
        this.baseIndex = new int[containers.length];
        int index = 0;
        for (int i = 0; i < containers.length; i++) {
            index += containers[i].getContainerSize();
            baseIndex[i] = index;
        }
        this.totalSlots = index;
    }

    private int getContainerIndexForSlot(int slot) {
        if (slot < 0) {
            return -1;
        }

        for (int i = 0; i < baseIndex.length; i++) {
            if (slot - baseIndex[i] < 0) {
                return i;
            }
        }

        return -1;
    }

    private Container getContainerFromIndex(int index) {
        return index >= 0 && index < containers.length ? containers[index] : EmptyContainer.INSTANCE;
    }

    private int getInnerSlotFromIndex(int slot, int index) {
        return index > 0 && index < baseIndex.length ? slot - baseIndex[index - 1] : slot;
    }

    private int getOuterSlotFromIndex(int slot, int index) {
        return index < baseIndex.length ? slot - baseIndex[index] : slot;
    }

    @Override
    public int getContainerSize() {
        return totalSlots;
    }

    @Override
    public boolean isEmpty() {
        return Arrays.stream(containers).allMatch(Container::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        return container.getItem(getInnerSlotFromIndex(slot, containerIndex));
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        return container.removeItem(getInnerSlotFromIndex(slot, containerIndex), amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        return container.removeItemNoUpdate(getInnerSlotFromIndex(slot, containerIndex));
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        container.setItem(getInnerSlotFromIndex(slot, containerIndex), itemStack);
    }

    @Override
    public void setChanged() {
        for (Container container : containers) {
            container.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Arrays.stream(containers).allMatch(container -> container.stillValid(player));
    }

    @Override
    public void clearContent() {
        for (Container container : containers) {
            container.clearContent();
        }
    }

    @Override
    public void startOpen(Player player) {
        for (Container container : containers) {
            container.startOpen(player);
        }
    }

    @Override
    public void stopOpen(Player player) {
        for (Container container : containers) {
            container.stopOpen(player);
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        return container.canPlaceItem(getInnerSlotFromIndex(slot, containerIndex), itemStack);
    }

    @Override
    public boolean canTakeItem(Container container, int slot, ItemStack itemStack) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container subContainer = getContainerFromIndex(containerIndex);
        return container.canTakeItem(subContainer, getInnerSlotFromIndex(slot, containerIndex), itemStack);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        Set<Integer> slots = new HashSet<>();
        for (int index = 0; index < containers.length; index++) {
            Container container = containers[index];
            if (container instanceof WorldlyContainer worldlyContainer) {
                for (int i : worldlyContainer.getSlotsForFace(direction)) {
                    slots.add(i);
                }
            } else {
                for (int i = 0; i < container.getContainerSize(); i++) {
                    slots.add(getOuterSlotFromIndex(i, index));
                }
            }
        }
        return slots.stream().mapToInt(i -> i).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        if (container instanceof WorldlyContainer worldlyContainer) {
            return worldlyContainer.canPlaceItemThroughFace(getInnerSlotFromIndex(slot, containerIndex), itemStack, direction);
        } else {
            return canPlaceItem(slot, itemStack);
        }
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        int containerIndex = getContainerIndexForSlot(slot);
        Container container = getContainerFromIndex(containerIndex);
        if (container instanceof WorldlyContainer worldlyContainer) {
            return worldlyContainer.canTakeItemThroughFace(slot, itemStack, direction);
        } else {
            return canTakeItem(this, slot, itemStack);
        }
    }
}
