package net.blay09.mods.balm.world;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Predicate;

public class DelegateContainer implements Container, WorldlyContainer {
    private final Container delegate;

    public DelegateContainer(Container delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getMaxStackSize() {
        return delegate.getMaxStackSize();
    }

    @Override
    public void startOpen(ContainerUser user) {
        delegate.startOpen(user);
    }

    @Override
    public void stopOpen(ContainerUser user) {
        delegate.stopOpen(user);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return delegate.canPlaceItem(slot, itemStack);
    }

    @Override
    public int countItem(Item item) {
        return delegate.countItem(item);
    }

    @Override
    public boolean hasAnyOf(Set<Item> items) {
        return delegate.hasAnyOf(items);
    }

    @Override
    public int getContainerSize() {
        return delegate.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return delegate.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return delegate.removeItem(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return delegate.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        delegate.setItem(slot, itemStack);
    }

    @Override
    public void setChanged() {
        delegate.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return delegate.stillValid(player);
    }

    @Override
    public void clearContent() {
        delegate.clearContent();
    }

    @Override
    public boolean canTakeItem(Container container, int slot, ItemStack itemStack) {
        return delegate.canTakeItem(this, slot, itemStack);
    }

    @Override
    public boolean hasAnyMatching(Predicate<ItemStack> predicate) {
        return delegate.hasAnyMatching(predicate);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (delegate instanceof WorldlyContainer worldContainer) {
            return worldContainer.getSlotsForFace(direction);
        }
        final var slots = new int[delegate.getContainerSize()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }
        return slots;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return delegate instanceof WorldlyContainer worldContainer ? worldContainer.canPlaceItemThroughFace(slot, itemStack, direction) : canPlaceItem(slot,
                itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return delegate instanceof WorldlyContainer worldContainer ? worldContainer.canTakeItemThroughFace(slot, itemStack, direction) : canTakeItem(this,
                slot,
                itemStack);
    }
}
